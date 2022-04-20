package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.logbook.ShootingSessionJfxModel;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import com.fligneul.srm.ui.service.logbook.ShootingLogbookServiceToJfxModel;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.Comparator;

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
    @FXML
    private GridPane shootingLogbookPane;
    @FXML
    private TextField shootingLogbookCreationDateTextField;
    @FXML
    private TextField shootingLogbookLastSessionDateTextField;

    private ShootingLogbookServiceToJfxModel shootingLogbookServiceToJfxModel;

    public AttendanceLicenseeSimpleNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    @Inject
    public void injectDependencies(final AttendanceSelectionService attendanceSelectionService,
                                   final ShootingLogbookServiceToJfxModel shootingLogbookServiceToJfxModel) {
        this.shootingLogbookServiceToJfxModel = shootingLogbookServiceToJfxModel;
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
        shootingLogbookCreationDateTextField.textProperty().unbind();
        shootingLogbookLastSessionDateTextField.textProperty().unbind();

        licenceNumberTextField.setText("");
        firstnameTextField.setText("");
        lastnameTextField.setText("");
        dateOfBirthTextField.setText("");
        maidenNameTextField.setText("");
        licenceStateTextField.setText("");
        firstLicenceDateTextField.setText("");
        seasonTextField.setText("");
        ageCategoryTextField.setText("");
        handisportCheckBox.setSelected(false);
        licenceBlacklistLabel.setVisible(false);
        licenceBlacklistLabel.setManaged(false);
        licenceErrorLabel.setVisible(false);
        licenceErrorLabel.setManaged(false);
        shootingLogbookPane.setManaged(false);
        shootingLogbookPane.setVisible(false);
        shootingLogbookCreationDateTextField.setText("");
        shootingLogbookLastSessionDateTextField.setText("");
    }

    private void updateComponents(final LicenseeJfxModel licenseeJfxModel) {
        setVisible(true);

        licenceNumberTextField.textProperty().bind(licenseeJfxModel.licenceNumberProperty());
        firstnameTextField.textProperty().bind(licenseeJfxModel.firstNameProperty());
        lastnameTextField.textProperty().bind(licenseeJfxModel.lastNameProperty());
        dateOfBirthTextField.textProperty().bind(Bindings.createStringBinding(() -> licenseeJfxModel.getDateOfBirth().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)), licenseeJfxModel.dateOfBirthProperty()));
        maidenNameTextField.textProperty().bind(licenseeJfxModel.maidenNameProperty());
        licenceStateTextField.textProperty().bind(licenseeJfxModel.licenceStateProperty());
        firstLicenceDateTextField.textProperty().bind(Bindings.createStringBinding(() -> Optional.ofNullable(licenseeJfxModel.getFirstLicenceDate())
                .map(date -> date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).orElse(""), licenseeJfxModel.firstLicenceDateProperty()));
        seasonTextField.textProperty().bind(licenseeJfxModel.seasonProperty());
        ageCategoryTextField.textProperty().bind(licenseeJfxModel.ageCategoryProperty());
        handisportCheckBox.selectedProperty().bind(licenseeJfxModel.handisportProperty());
        licenceBlacklistLabel.visibleProperty().bind(licenseeJfxModel.blacklistedProperty());
        licenceBlacklistLabel.managedProperty().bind(licenseeJfxModel.blacklistedProperty());

        BooleanBinding licenceReceiptIncomplete = licenseeJfxModel.medicalCertificateDateProperty().isNull().or(
                licenseeJfxModel.idCardDateProperty().isNull().or(licenseeJfxModel.idPhotoProperty().not()));
        licenceErrorLabel.visibleProperty().bind(licenceReceiptIncomplete);
        licenceErrorLabel.managedProperty().bind(licenceReceiptIncomplete);

        shootingLogbookServiceToJfxModel.getShootingLogbookList().stream()
                .filter(logbook -> logbook.getLicenseeId() == licenseeJfxModel.getId())
                .findFirst()
                .ifPresent(shootingLogbookJfxModel -> {
                    shootingLogbookPane.setManaged(true);
                    shootingLogbookPane.setVisible(true);
                    shootingLogbookCreationDateTextField.textProperty().bind(Bindings.createStringBinding(() -> shootingLogbookJfxModel.getCreationDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)), shootingLogbookJfxModel.creationDateProperty()));
                    shootingLogbookLastSessionDateTextField.textProperty().bind(Bindings.createStringBinding(() -> shootingLogbookJfxModel.getSessions().stream()
                            .map(ShootingSessionJfxModel::getSessionDate)
                            .max(Comparator.comparing(LocalDate::toEpochDay))
                            .map(date -> date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))
                            .orElse(""), shootingLogbookJfxModel.sessionsProperty()));
                });
    }
}
