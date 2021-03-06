package com.fligneul.srm.ui.node.settings.items;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.BackupService;
import com.fligneul.srm.service.ShutdownService;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.File;

/**
 * Application backup node
 */
public class BackupSettingsNode extends StackPane implements ISettingsItemNode {
    private static final Logger LOGGER = LogManager.getLogger(FiringPointSettingsNode.class);

    private static final String FXML_PATH = "backupSettings.fxml";
    private static final String TITLE = "Sauvegarde & Restauration";

    private BackupService backupService;
    private ShutdownService shutdownService;

    public BackupSettingsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    /**
     * Inject GUICE dependencies
     *
     * @param backupService
     *         DB backup service
     * @param shutdownService
     *         service for graceful shutdown
     */
    @Inject
    public void injectDependencies(final BackupService backupService,
                                   final ShutdownService shutdownService) {
        this.backupService = backupService;
        this.shutdownService = shutdownService;
    }

    @FXML
    private void backup() {
        LOGGER.trace("Backup button clicked");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarde");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(backupService.getBackupName());
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Shooting Range Manager backup", "*.srmb"));
        File file = fileChooser.showSaveDialog(getScene().getWindow());
        if (file != null) {
            if (backupService.backupDatabase(file)) {
                LOGGER.info("DB saved in {}, user must login again", file.getPath());
                DialogUtils.showInformationDialog("Sauvegarde", null, "Sauvegarde effectu??e avec succ??s.\nMerci de vous reconnecter ?? l'application.");
            } else {
                LOGGER.error("DB not saved");
                DialogUtils.showErrorDialog("Sauvegarde", null, "Erreur lors de la sauvegarde de la base de donn??es.\nVeuillez contacter un administrateur.");
            }
        }
    }

    @FXML
    private void restore() {
        LOGGER.trace("Restore button clicked");

        DialogUtils.showConfirmationDialog("Restauration", null, "Restaurer une sauvegarde n??cessite le red??marrage de l'application.\nVoulez-vous continuer ?", () -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Restauration");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Shooting Range Manager backup", "*.srmb"));
            File file = fileChooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                if (backupService.restoreDatabase(file)) {
                    LOGGER.info("DB restored from {}", file.getPath());
                    DialogUtils.showInformationDialog("Restauration", null, "Restauration effectu??e avec succ??s.\nL'application va d??sormais s'arr??ter.");
                } else {
                    LOGGER.error("DB not restored");
                    DialogUtils.showErrorDialog("Restauration", null, "Erreur lors de la restauration de la sauvegarde.\nVeuillez contacter un administrateur.");
                }
                shutdownService.shutdown();
            }
        });
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public Node getNode() {
        return this;
    }
}
