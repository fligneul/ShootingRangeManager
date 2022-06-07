package com.fligneul.srm.service;

import com.fligneul.srm.dao.user.UserDAO;
import com.fligneul.srm.ui.model.user.ERole;
import com.fligneul.srm.ui.model.user.UserJfxModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * User service
 */
public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnectionService.class);

    private final BehaviorSubject<List<UserJfxModel>> userSubject = BehaviorSubject.createDefault(List.of());
    private DatabaseConnectionService databaseConnectionService;
    private AuthenticationService authenticationService;
    private UserDAO userDAO;

    /**
     * Inject GUICE dependencies
     *
     * @param databaseConnectionService
     *         connection service to the DB
     * @param authenticationService
     *         user authentication service
     * @param userDAO
     *         DAO for user table
     */
    @Inject
    private void injectDependencies(final DatabaseConnectionService databaseConnectionService,
                                    final AuthenticationService authenticationService,
                                    final UserDAO userDAO) {
        this.databaseConnectionService = databaseConnectionService;
        this.authenticationService = authenticationService;
        this.userDAO = userDAO;

        this.userSubject.onNext(userDAO.getAll());
    }

    /**
     * Register a new user in the DB
     *
     * @param username
     *         username
     * @param passwd
     *         user password
     * @param isAdmin
     *         {@code true} if the user is an administrator, {@code false} otherwise
     */
    public void registerUser(String username, char[] passwd, boolean isAdmin) {
        try {
            PreparedStatement z = databaseConnectionService.getConnection()
                    .prepareStatement(
                            "CREATE USER " + username.toUpperCase() + " PASSWORD " + "'" + String.valueOf(passwd) + "' ADMIN"
                    );

            z.execute();

            userDAO.save(new UserJfxModel(username, isAdmin ? ERole.ADMINISTRATOR : ERole.USER))
                    .ifPresent(user -> {
                        List<UserJfxModel> userModels = userSubject.getValue();
                        userModels.add(user);
                        userSubject.onNext(userModels);
                    });

            LOGGER.info("User {} successfully created", username);
        } catch (SQLException e) {
            LOGGER.error("Error during licensee get", e);
        }

    }

    /**
     * Delete the provided user of the database.
     * Warning: for security reasons, the current user can't be removed
     *
     * @param name
     *         username to delete
     */
    public void deleteUser(String name) {
        authenticationService.getUserAuthenticatedObs()
                .firstElement()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .subscribe(currentUser -> {
                            if (currentUser.equalsIgnoreCase(name)) {
                                LOGGER.error("Can't remove the current user");
                            } else {
                                databaseConnectionService.getConnection()
                                        .prepareCall("DROP USER " + name.toUpperCase())
                                        .execute();
                                userDAO.deleteByName(name);
                                List<UserJfxModel> userJfxModels = userSubject.getValue();
                                userJfxModels.removeIf(user -> user.getName().equalsIgnoreCase(name));
                                userSubject.onNext(userJfxModels);
                            }
                        }, e -> LOGGER.error("Error during user {} deletion", name, e),
                        () -> LOGGER.error("Can't remove an unknown user {}", name));
    }

    /**
     * @return an observable of all users in the DB
     */
    public @NonNull Observable<List<UserJfxModel>> getUserObs() {
        return userSubject.hide();
    }

    /**
     * Update the current user password
     *
     * @param passwd
     *         the new password
     * @return {@code true} if the update is successfull, {@code false  otherwise
     */
    public boolean updatePassword(final char[] passwd) {
        try {
            PreparedStatement z = databaseConnectionService.getConnection()
                    .prepareStatement(
                            "SET PASSWORD '" + String.valueOf(passwd) + "'"
                    );

            z.execute();
            return true;

        } catch (SQLException e) {
            LOGGER.error("Error during password update get", e);
            return false;
        }
    }
}
