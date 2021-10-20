package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class AttendanceLicenseeSelectorNode extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceLicenseeSelectorNode.class);
    private static final String FXML_PATH = "licenseeSelector.fxml";

    @FXML
    private TextField licenceNumber;
    @FXML
    private Button validate;
    @FXML
    private Label errorLabel;

    private AttendanceSelectionService attendanceSelectionService;

    public AttendanceLicenseeSelectorNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        licenceNumber.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                validate.fire();
            }
        });

        Platform.runLater(() -> licenceNumber.requestFocus());
    }

    @Inject
    public void injectDependencies(final AttendanceSelectionService attendanceSelectionService) {
        this.attendanceSelectionService = attendanceSelectionService;
    }

    @FXML
    private void searchLicensee() {
        attendanceSelectionService.clearSelected();
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        try {
            if (attendanceSelectionService.searchAndSelect(licenceNumber.getText().split("\\|")[0])) {
                licenceNumber.clear();
            } else {
                errorLabel.setText("Aucun licencié enregistré avec ce numéro de licence");
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Numéro de licence invalide");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
        }
    }
}
