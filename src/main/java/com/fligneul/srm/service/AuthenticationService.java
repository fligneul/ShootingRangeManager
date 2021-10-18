package com.fligneul.srm.service;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

/**
 * User authentication service
 */
public class AuthenticationService {
    private static final Logger LOGGER = LogManager.getLogger(AuthenticationService.class);

    private final Subject<Boolean> authenticatedSubject = BehaviorSubject.createDefault(false);

    @Inject
    private void injectDependencies() {
    }

    /**
     * Mocked authentication;
     * Use {@code azerty} username to login
     *
     * @param username
     *         user name
     * @param passwd
     *         user password
     * @return the error message if the authentication fail
     */
    public Optional<String> authenticate(final String username, final char[] passwd) {
        if (username.equals("azerty")) {
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            authenticatedSubject.onNext(true);
            return Optional.empty();
        } else {
            authenticatedSubject.onNext(false);
            return Optional.of("Connexion refusée");
        }
    }

    /**
     * Disconnect current logged user
     */
    public void disconnect() {
        authenticatedSubject.onNext(false);
    }

    /**
     * The authentication subject, {@code true} if a user is logged, false otherwise
     *
     * @return the authentication subject, {@code true} if a user is logged, false otherwise
     */
    public Observable<Boolean> getAuthenticatedObs() {
        return authenticatedSubject;
    }
}
