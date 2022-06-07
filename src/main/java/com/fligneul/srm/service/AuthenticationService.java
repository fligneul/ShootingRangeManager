package com.fligneul.srm.service;

import com.fligneul.srm.dao.user.UserDAO;
import com.fligneul.srm.ui.model.user.ERole;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import liquibase.exception.LiquibaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbc.JdbcSQLInvalidAuthorizationSpecException;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User authentication service
 */
public class AuthenticationService {
    private static final Logger LOGGER = LogManager.getLogger(AuthenticationService.class);

    private final Subject<Boolean> authenticatedSubject = BehaviorSubject.createDefault(false);
    private final Subject<Optional<String>> userAuthenticatedSubject = BehaviorSubject.createDefault(Optional.empty());
    private DatabaseConnectionService databaseConnectionService;
    private UserDAO userDAO;
    private RoleService roleService;

    /**
     * Inject GUICE dependencies
     *
     * @param databaseConnectionService
     *         connection service to the DB
     * @param userDAO
     *         DAO for user table
     * @param roleService
     *         DAO for firing point table
     */
    @Inject
    private void injectDependencies(final DatabaseConnectionService databaseConnectionService,
                                    final UserDAO userDAO,
                                    final RoleService roleService) {
        this.databaseConnectionService = databaseConnectionService;
        this.userDAO = userDAO;
        this.roleService = roleService;
    }

    /**
     * User authentication
     *
     * @param username
     *         username
     * @param passwd
     *         user password
     * @return the error message if the authentication fail
     */
    public Optional<String> authenticate(final String username, final char[] passwd) {
        try {
            Connection connection = databaseConnectionService.initConnection(username, passwd);
            connection.isValid(1_000);
            AtomicReference<String> message = new AtomicReference<>();
            userDAO.getByName(username.toUpperCase())
                    .ifPresentOrElse(user -> {
                        authenticatedSubject.onNext(true);
                        userAuthenticatedSubject.onNext(Optional.of(user.getName()));
                        roleService.publish(user.getRole());
                    }, () -> {
                        authenticatedSubject.onNext(false);
                        userAuthenticatedSubject.onNext(Optional.empty());
                        roleService.publish(ERole.NONE);
                        message.set("Connexion refusée");
                    });
            return Optional.ofNullable(message.get());
        } catch (JdbcSQLInvalidAuthorizationSpecException e) {
            LOGGER.warn("Unauthorized user {}", username, e);
            return Optional.of("Utilisateur inconnu");
        } catch (SQLException | LiquibaseException e) {
            LOGGER.error("Connection error", e);
            return Optional.of("Erreur de connexion à la base de données");
        }
    }

    /**
     * Disconnect current logged user
     */
    public void disconnect() {
        authenticatedSubject.onNext(false);
        userAuthenticatedSubject.onNext(Optional.empty());
        roleService.publish(ERole.NONE);
        databaseConnectionService.closeConnection();
    }

    /**
     * The authentication subject, {@code true} if a user is logged, false otherwise
     *
     * @return the authentication subject, {@code true} if a user is logged, false otherwise
     */
    public Observable<Boolean> getAuthenticatedObs() {
        return authenticatedSubject;
    }

    /**
     * The authenticated user subject, if a user is logged, it contained the current username, empty otherwise
     *
     * @return the authenticated user subject, if a user is logged, it contained the current username, empty otherwise
     */
    public Observable<Optional<String>> getUserAuthenticatedObs() {
        return userAuthenticatedSubject;
    }

}
