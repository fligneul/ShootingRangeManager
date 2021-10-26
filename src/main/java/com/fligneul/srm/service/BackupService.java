package com.fligneul.srm.service;

import net.lingala.zip4j.ZipFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Backup and restore service
 */
public class BackupService {
    private static final Logger LOGGER = LogManager.getLogger(BackupService.class);

    private static final String BACKUP_DATE_PATTERN = "yyyy-MM-dd_HH-mm-ss";
    private static final String BACKUP_FILE_EXTENSION = "srmb";

    private DatabaseConnectionService databaseConnectionService;
    private AuthenticationService authenticationService;

    @Inject
    private void injectDependencies(DatabaseConnectionService databaseConnectionService,
                                    AuthenticationService authenticationService) {
        this.databaseConnectionService = databaseConnectionService;
        this.authenticationService = authenticationService;
    }

    /**
     * Return the backup file extension
     *
     * @return the backup file extension
     */
    public String getBackupExtension() {
        return BACKUP_FILE_EXTENSION;
    }

    /**
     * Generate the backup filename
     *
     * @return the backup filename
     */
    public String getBackupName() {
        return String.format("backup_%s.%s", DateTimeFormatter.ofPattern(BACKUP_DATE_PATTERN).format(LocalDateTime.now()), getBackupExtension());
    }

    /**
     * Backup the current DB to the provided file
     *
     * @param destinationFile
     *         the desired backup file
     * @return {@code true} if the backup is successful, false otherwise
     */
    public boolean backupDatabase(final File destinationFile) {
        try (ZipFile backupFile = new ZipFile(destinationFile)) {
            databaseConnectionService.closeConnection();
            backupFile.addFiles(databaseConnectionService.getDatabaseFiles());
            return backupFile.isValidZipFile();
        } catch (IOException e) {
            LOGGER.error("Error during backup", e);
            return false;
        } finally {
            authenticationService.disconnect();
        }
    }

    /**
     * Restore DB from provided file
     * Warning : the application must be restarted after this method
     *
     * @param backupFile
     *         DB backup file
     * @return {@code true} if the restoration is successful, false otherwise
     */
    public boolean restoreDatabase(final File backupFile) {
        try (ZipFile restoreFile = new ZipFile(backupFile)) {
            if (!restoreFile.isValidZipFile() || restoreFile.isEncrypted()) {
                LOGGER.error("Impossible to restore DB from {}", restoreFile.getFile().getPath());
                return false;
            }
            databaseConnectionService.closeConnection();
            restoreFile.extractAll(databaseConnectionService.getDatabaseDirectory().getAbsolutePath());
            LOGGER.info("Successful restoration");
            return true;
        } catch (IOException e) {
            LOGGER.error("Error during restoration", e);
            return false;
        }
    }
}
