package com.fligneul.srm.ui.node.logbook;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.ValidatedDatePicker;
import com.fligneul.srm.ui.model.logbook.ShootingLogbookJfxModel;
import com.fligneul.srm.ui.node.licensee.LicenseeDetailNode;
import com.fligneul.srm.ui.service.logbook.ShootingLogbookServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Node for shooting logbook creation
 * It contains a DatePicker for the creation date and a save button
 */
public class ShootingLogbookCreateNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(LicenseeDetailNode.class);
    private static final String FXML_PATH = "shootingLogbookCreate.fxml";

    @FXML
    private ValidatedDatePicker creationDatePicker;
    @FXML
    private Button saveButton;

    private ShootingLogbookServiceToJfxModel shootingLogbookServiceToJfxModel;
    private final int licenseeId;

    public ShootingLogbookCreateNode(final int licenseeId) {
        this.licenseeId = licenseeId;
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        creationDatePicker.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                creationDatePicker.setValue(creationDatePicker.getConverter().fromString(creationDatePicker.getEditor().getText()));
            }
        });

        saveButton.disableProperty().bind(creationDatePicker.isValidProperty().not());
    }

    /**
     * Inject GUICE dependencies
     *
     * @param shootingLogbookServiceToJfxModel
     *         service to jfx model for shooting logbook
     */
    @Inject
    public void injectDependencies(final ShootingLogbookServiceToJfxModel shootingLogbookServiceToJfxModel) {
        this.shootingLogbookServiceToJfxModel = shootingLogbookServiceToJfxModel;
    }

    @FXML
    private void saveShootingLogbook() {
        if (creationDatePicker.getValue() != null) {
            shootingLogbookServiceToJfxModel.saveShootingLogbook(new ShootingLogbookJfxModel(creationDatePicker.getValue(), licenseeId));
            closeStage();
        }
    }

    @FXML
    private void cancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }
}
