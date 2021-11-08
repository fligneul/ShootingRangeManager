package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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

    public AttendanceLicenseeSimpleNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

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
        licenceBlacklistLabel.managedProperty().unbind();

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
        licenceBlacklistLabel.setManaged(false);
    }

    private void updateComponents(final LicenseeJfxModel licenseeJfxModel) {
        setVisible(true);

        licenceNumberTextField.textProperty().bind(licenseeJfxModel.licenceNumberProperty());
        firstnameTextField.textProperty().bind(licenseeJfxModel.firstNameProperty());
        lastnameTextField.textProperty().bind(licenseeJfxModel.lastNameProperty());
        dateOfBirthTextField.textProperty().bind(Bindings.createStringBinding(() -> licenseeJfxModel.getDateOfBirth().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)), licenseeJfxModel.dateOfBirthProperty()));
        maidenNameTextField.textProperty().bind(licenseeJfxModel.maidenNameProperty());
        licenceStateTextField.textProperty().bind(licenseeJfxModel.licenceStateProperty());
        firstLicenceDateTextField.textProperty().bind(Bindings.createStringBinding(() -> licenseeJfxModel.getFirstLicenceDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)), licenseeJfxModel.firstLicenceDateProperty()));
        seasonTextField.textProperty().bind(licenseeJfxModel.seasonProperty());
        ageCategoryTextField.textProperty().bind(licenseeJfxModel.ageCategoryProperty());
        handisportCheckBox.selectedProperty().bind(licenseeJfxModel.handisportProperty());
        licenceBlacklistLabel.managedProperty().bind(licenseeJfxModel.blacklistedProperty());
    }
}
