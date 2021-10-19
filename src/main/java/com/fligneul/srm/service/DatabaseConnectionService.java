package com.fligneul.srm.service;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handle connection to the database
 */
public class DatabaseConnectionService {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnectionService.class);

    private static final String DATABASE_PATH = "./database";
    private static final String URL = "jdbc:h2:file:" + DATABASE_PATH;
    private static final String DB_EXTENSION_REGEX = ".*.db";
    private static final String DB_CHANGELOG_MASTER_XML = "com/fligneul/srm/db/db.changelog-master.xml";

    private Connection connect;
    private DSLContext context;

    /**
     * Check on the filesystem if the DB is already created
     *
     * @return {@code true} if the DB is already created
     */
    public boolean isDatabaseCreated() {
        Path databasePath = Path.of(DATABASE_PATH);
        try {
            return Files.find(databasePath.getParent(), 1, (path, basicFileAttributes) -> path.toFile().getName().matches(databasePath.getFileName() + DB_EXTENSION_REGEX)).findAny().isPresent();
        } catch (IOException e) {
            LOGGER.debug("Error during DB search", e);
            return false;
        }
    }

    /**
     * Initiate DB connection with the provided credentials, run liquibase update and instantiate JOOQ context
     *
     * @param user
     *         username
     * @param passwd
     *         password
     * @return the JDBC connection
     * @throws SQLException
     *         an exception on JDBC connection
     * @throws LiquibaseException
     *         an exception during Liquibase update
     */
    public Connection initConnection(String user, char[] passwd) throws SQLException, LiquibaseException {
        connect = DriverManager.getConnection(URL, user, String.valueOf(passwd));
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connect));
        Liquibase liquibase = new Liquibase(DB_CHANGELOG_MASTER_XML, new ClassLoaderResourceAccessor(), database);
        liquibase.update(new Contexts(), new LabelExpression());
        context = DSL.using(connect, SQLDialect.H2);
        return connect;
    }

    /**
     * Close the current JDBC connection
     */
    public void closeConnection() {
        try {
            connect.commit();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connect = null;
    }

    /**
     * Return the JDBC connection
     * If the connection is not initialized, a {@link IllegalStateException} is thrown
     *
     * @return the JDBC connection
     */
    public Connection getConnection() {
        if (connect == null) {
            throw new IllegalStateException("Should not get connection if not instantiated");
        }
        return connect;
    }

    /**
     * Return the JOOQ context
     * If the context is not initialized, a {@link IllegalStateException} is thrown
     *
     * @return the JOOQ context
     */
    public DSLContext getContext() {
        if (context == null) {
            throw new IllegalStateException("Should not get context if not instantiated");
        }
        return context;
    }
}
