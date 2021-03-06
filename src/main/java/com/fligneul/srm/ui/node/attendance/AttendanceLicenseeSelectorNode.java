package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.node.utils.InputUtils;
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

/**
 * Licensee selector node for attendance
 */
public class AttendanceLicenseeSelectorNode extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceLicenseeSelectorNode.class);
    private static final String FXML_PATH = "licenseeSelector.fxml";

    @FXML
    protected TextField licenceNumber;
    @FXML
    protected Button validate;
    @FXML
    protected Label errorLabel;

    private AttendanceSelectionService attendanceSelectionService;

    /**
     * Create the node and load the associated FXML file
     */
    public AttendanceLicenseeSelectorNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        licenceNumber.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                validate.fire();
            }
        });

        Platform.runLater(() -> licenceNumber.requestFocus());
    }

    /**
     * Inject GUICE dependencies
     *
     * @param attendanceSelectionService
     *         selection service for the current licensee
     */
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
            if (attendanceSelectionService.searchAndSelect(InputUtils.parseLicenceNumber(licenceNumber.getText()))) {
                licenceNumber.clear();
            } else {
                errorLabel.setText("Aucun licenci?? enregistr?? avec ce num??ro de licence");
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            }
        } catch (IllegalArgumentException e) {
            errorLabel.setText("Num??ro de licence invalide");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
        }
    }
}
