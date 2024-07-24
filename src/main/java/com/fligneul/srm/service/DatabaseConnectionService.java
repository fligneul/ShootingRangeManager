package com.fligneul.srm.service;

import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle connection to the database
 */
public class DatabaseConnectionService {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnectionService.class);

    private static final String DATABASE_PATH = "./database";
    private static final String URL = "jdbc:h2:file:" + DATABASE_PATH + ";TRACE_LEVEL_FILE=0";
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
        return !getDatabaseFiles().isEmpty();
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
        try {
            Scope.child(Scope.Attr.resourceAccessor,
                    new ClassLoaderResourceAccessor(), () -> {
                        CommandScope cmd = new CommandScope("update");
                        cmd.addArgumentValue("database", database);
                        cmd.addArgumentValue("changelogFile", DB_CHANGELOG_MASTER_XML);
                        cmd.execute();
                    });
        } catch (Exception e) {
            throw new LiquibaseException(e);
        }
        context = DSL.using(connect, SQLDialect.H2, new Settings().withRenderNameCase(RenderNameCase.UPPER));
        return connect;
    }

    /**
     * Close the current JDBC connection
     */
    public void closeConnection() {
        if (connect != null) {
            try {
                connect.commit();
                connect.close();
            } catch (SQLException e) {
                LOGGER.error("Error during DB close, e");
            }
            connect = null;
        }
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

    /**
     * Return the DB files on the filesystem
     *
     * @return list of all DB files
     */
    protected List<File> getDatabaseFiles() {
        Path databasePath = Path.of(DATABASE_PATH);
        try (var dbFiles = Files.find(databasePath.getParent(), 1, (path, basicFileAttributes) -> path.toFile().getName().matches(databasePath.getFileName() + DB_EXTENSION_REGEX))) {
            return dbFiles.map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.debug("Error during DB file search", e);
            return List.of();
        }
    }

    /**
     * Return the DB directory on the filesystem
     *
     * @return the DB directory
     */
    protected File getDatabaseDirectory() {
        return Path.of(DATABASE_PATH).getParent().toFile();
    }
}
