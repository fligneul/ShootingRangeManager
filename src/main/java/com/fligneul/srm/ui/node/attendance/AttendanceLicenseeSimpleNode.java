package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.node.utils.FormatterUtils;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * Licensee simple detail node for attendance node.
 * This node only display basic information about the licensee
 */
public class AttendanceLicenseeSimpleNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceLicenseeSimpleNode.class);
    private static final String FXML_PATH = "licenseeSimple.fxml";

    @FXML
    private TextField licenceNumberTextField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField dateOfBirthTextField;
    @FXML
    private TextField maidenNameTextField;
    @FXML
    private CheckBox handisportCheckBox;
    @FXML
    private Label licenceBlacklistLabel;
    @FXML
    private TextField licenceStateTextField;
    @FXML
    private TextField firstLicenceDateTextField;
    @FXML
    private TextField seasonTextField;
    @FXML
    private TextField ageCategoryTextField;
    @FXML
    private Label licenceErrorLabel;

    /**
     * Create the node and load the associated FXML file
     */
    public AttendanceLicenseeSimpleNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    /**
     * Inject GUICE dependencies
     *
     * @param attendanceSelectionService
     *         selection service for the current licensee
     */
    @Inject
    public void injectDependencies(final AttendanceSelectionService attendanceSelectionService) {
        attendanceSelectionService.selectedObs()
                .distinctUntilChanged()
                .observeOn(JavaFxScheduler.platform())
                .doOnNext(s -> LOGGER.debug("Select licensee " + s.map(LicenseeJfxModel::getLicenceNumber).orElse("null")))
                .subscribe(optLicensee -> optLicensee.ifPresentOrElse(this::updateComponents, this::clearComponents), LOGGER::error);
    }

    private void clearComponents() {
        setVisible(false);

        licenceNumberTextField.textProperty().unbind();
        firstnameTextField.textProperty().unbind();
        lastnameTextField.textProperty().unbind();
        dateOfBirthTextField.textProperty().unbind();
        maidenNameTextField.textProperty().unbind();
        licenceStateTextField.textProperty().unbind();
        firstLicenceDateTextField.textProperty().unbind();
        seasonTextField.textProperty().unbind();
        ageCategoryTextField.textProperty().unbind();
        handisportCheckBox.selectedProperty().unbind();
        licenceBlacklistLabel.visibleProperty().unbind();
        licenceBlacklistLabel.managedProperty().unbind();
        licenceErrorLabel.managedProperty().unbind();
        licenceErrorLabel.visibleProperty().unbind();

        licenceNumberTextField.setText(EMPTY);
        firstnameTextField.setText(EMPTY);
        lastnameTextField.setText(EMPTY);
        dateOfBirthTextField.setText(EMPTY);
        maidenNameTextField.setText(EMPTY);
        licenceStateTextField.setText(EMPTY);
        firstLicenceDateTextField.setText(EMPTY);
        seasonTextField.setText(EMPTY);
        ageCategoryTextField.setText(EMPTY);
        handisportCheckBox.setSelected(false);
        licenceBlacklistLabel.setVisible(false);
        licenceBlacklistLabel.setManaged(false);
        licenceErrorLabel.setVisible(false);
        licenceErrorLabel.setManaged(false);
    }

    private void updateComponents(final LicenseeJfxModel licenseeJfxModel) {
        setVisible(true);

        licenceNumberTextField.textProperty().bind(licenseeJfxModel.licenceNumberProperty());
        firstnameTextField.textProperty().bind(licenseeJfxModel.firstNameProperty());
        lastnameTextField.textProperty().bind(licenseeJfxModel.lastNameProperty());
        dateOfBirthTextField.textProperty().bind(Bindings.createStringBinding(() -> FormatterUtils.formatDate(licenseeJfxModel.getDateOfBirth()), licenseeJfxModel.dateOfBirthProperty()));
        maidenNameTextField.textProperty().bind(licenseeJfxModel.maidenNameProperty());
        licenceStateTextField.textProperty().bind(licenseeJfxModel.licenceStateProperty());
        firstLicenceDateTextField.textProperty().bind(Bindings.createStringBinding(() -> FormatterUtils.formatDate(licenseeJfxModel.getFirstLicenceDate()), licenseeJfxModel.firstLicenceDateProperty()));
        seasonTextField.textProperty().bind(licenseeJfxModel.seasonProperty());
        ageCategoryTextField.textProperty().bind(licenseeJfxModel.ageCategoryProperty());
        handisportCheckBox.selectedProperty().bind(licenseeJfxModel.handisportProperty());
        licenceBlacklistLabel.visibleProperty().bind(licenseeJfxModel.blacklistedProperty());
        licenceBlacklistLabel.managedProperty().bind(licenseeJfxModel.blacklistedProperty());

        BooleanBinding licenceReceiptIncomplete = licenseeJfxModel.medicalCertificateDateProperty().isNull()
                .or(licenseeJfxModel.idCardDateProperty().isNull())
                .or(licenseeJfxModel.idPhotoProperty().not());
        licenceErrorLabel.visibleProperty().bind(licenceReceiptIncomplete);
        licenceErrorLabel.managedProperty().bind(licenceReceiptIncomplete);
    }
}
