package com.fligneul.srm.service;

import com.fligneul.srm.dao.user.UserDAO;
import com.fligneul.srm.ui.model.user.ERole;
import com.fligneul.srm.ui.model.user.UserJfxModel;
import liquibase.exception.LiquibaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.sql.SQLException;

/**
 * Handle first start of the application
 * It creates the first administrator user and persist it
 */
public class FirstStartService {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnectionService.class);

    private DatabaseConnectionService databaseConnectionService;
    private UserDAO userDAO;

    @Inject
    private void injectDependencies(final DatabaseConnectionService databaseConnectionService,
                                    final UserDAO userDAO) {
        this.databaseConnectionService = databaseConnectionService;
        this.userDAO = userDAO;
    }

    public boolean createFirstUser(final String username, char[] passwd) {
        try {
            databaseConnectionService.initConnection(username, passwd);
            userDAO.save(new UserJfxModel(username, ERole.ADMINISTRATOR));
            LOGGER.info("First user {} successfully created", username);
            return true;
        } catch (SQLException | LiquibaseException e) {
            LOGGER.error("Error during first user creation", e);
            return false;
        }
    }

}
