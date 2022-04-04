package com.fligneul.srm.ui.node.licensee;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

public class LicenseeCreateNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(LicenseeCreateNode.class);
    private static final String FXML_PATH = "licenseeCreate.fxml";

    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ImageView profileImage;
    @FXML
    private TextField licenceNumberTextField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private TextField maidenNameTextField;
    @FXML
    private TextField placeOfBirthTextField;
    @FXML
    private TextField departmentOfBirthTextField;
    @FXML
    private TextField countryOfBirthTextField;
    @FXML
    private CheckBox handisportCheckBox;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField zipCodeTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private CheckBox blacklistCheckBox;
    @FXML
    private TextField licenceStateTextField;
    @FXML
    private DatePicker firstLicenceDatePicker;
    @FXML
    private TextField seasonTextField;
    @FXML
    private TextField ageCategoryTextField;
    @FXML
    private DatePicker medicalCertificateDatePicker;
    @FXML
    private DatePicker idCardDatePicker;
    @FXML
    private CheckBox idPhotoCheckBox;

    private LicenseeServiceToJfxModel licenseeServiceToJfxModel;

    private LicenseeJfxModel currentLicenseeJfxModel;

    public LicenseeCreateNode(final LicenseeJfxModel licenseeJfxModel) {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        Optional.ofNullable(getClass().getResourceAsStream("/blank.png"))
                .ifPresent(is -> this.profileImage.setImage(new Image(is)));

        Optional.ofNullable(licenseeJfxModel).ifPresent(this::updateComponents);

        dateOfBirthPicker.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                dateOfBirthPicker.setValue(dateOfBirthPicker.getConverter().fromString(dateOfBirthPicker.getEditor().getText()));
            }
        });

        firstLicenceDatePicker.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                firstLicenceDatePicker.setValue(firstLicenceDatePicker.getConverter().fromString(firstLicenceDatePicker.getEditor().getText()));
            }
        });

        medicalCertificateDatePicker.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                medicalCertificateDatePicker.setValue(medicalCertificateDatePicker.getConverter().fromString(medicalCertificateDatePicker.getEditor().getText()));
            }
        });

        idCardDatePicker.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                idCardDatePicker.setValue(idCardDatePicker.getConverter().fromString(idCardDatePicker.getEditor().getText()));
            }
        });
    }

    public LicenseeCreateNode() {
        this(null);
    }

    @Inject
    public void injectDependencies(final LicenseeServiceToJfxModel licenseeServiceToJfxModel) {
        this.licenseeServiceToJfxModel = licenseeServiceToJfxModel;
    }

    private void clearComponents() {
        currentLicenseeJfxModel = null;

        licenceNumberTextField.setText("");
        firstnameTextField.setText("");
        lastnameTextField.setText("");
        dateOfBirthPicker.getEditor().setText("");
        maidenNameTextField.setText("");
        placeOfBirthTextField.setText("");
        departmentOfBirthTextField.setText("");
        countryOfBirthTextField.setText("");
        addressTextField.setText("");
        zipCodeTextField.setText("");
        cityTextField.setText("");
        emailTextField.setText("");
        phoneNumberTextField.setText("");
        licenceStateTextField.setText("");
        firstLicenceDatePicker.getEditor().setText("");
        seasonTextField.setText("");
        ageCategoryTextField.setText("");
        medicalCertificateDatePicker.getEditor().setText("");
        idCardDatePicker.getEditor().setText("");
        handisportCheckBox.setSelected(false);
        blacklistCheckBox.setSelected(false);
        idPhotoCheckBox.setSelected(false);
    }

    private void updateComponents(LicenseeJfxModel licenseeJfxModel) {
        currentLicenseeJfxModel = licenseeJfxModel;

        licenceNumberTextField.setText(licenseeJfxModel.getLicenceNumber());
        firstnameTextField.setText(licenseeJfxModel.getFirstName());
        lastnameTextField.setText(licenseeJfxModel.getLastName());
        dateOfBirthPicker.setValue(licenseeJfxModel.getDateOfBirth());
        maidenNameTextField.setText(licenseeJfxModel.getMaidenName());
        placeOfBirthTextField.setText(licenseeJfxModel.getPlaceOfBirth());
        departmentOfBirthTextField.setText(licenseeJfxModel.getDepartmentOfBirth());
        countryOfBirthTextField.setText(licenseeJfxModel.getCountryOfBirth());
        addressTextField.setText(licenseeJfxModel.getAddress());
        zipCodeTextField.setText(licenseeJfxModel.getZipCode());
        cityTextField.setText(licenseeJfxModel.getCity());
        emailTextField.setText(licenseeJfxModel.getEmail());
        phoneNumberTextField.setText(licenseeJfxModel.getPhoneNumber());
        licenceStateTextField.setText(licenseeJfxModel.getLicenceState());
        firstLicenceDatePicker.setValue(licenseeJfxModel.getFirstLicenceDate());
        seasonTextField.setText(licenseeJfxModel.getSeason());
        ageCategoryTextField.setText(licenseeJfxModel.getAgeCategory());
        medicalCertificateDatePicker.setValue(licenseeJfxModel.getMedicalCertificateDate());
        idCardDatePicker.setValue(licenseeJfxModel.getIdCardDate());
        handisportCheckBox.setSelected(licenseeJfxModel.isHandisport());
        blacklistCheckBox.setSelected(licenseeJfxModel.isBlacklisted());
        idPhotoCheckBox.setSelected(licenseeJfxModel.hasIdPhoto());
    }

    @FXML
    private void saveLicensee() {
        final LicenseeJfxModelBuilder builder = new LicenseeJfxModelBuilder();

        builder.setId((currentLicenseeJfxModel != null ? currentLicenseeJfxModel.getId() : -1))
                .setFirstName(firstnameTextField.getText())
                .setLastName(lastnameTextField.getText())
                .setDateOfBirth(dateOfBirthPicker.getValue())
                .setHandisport(handisportCheckBox.isSelected())
                .setBlacklisted(blacklistCheckBox.isSelected())
                .setIdPhoto(idPhotoCheckBox.isSelected());

        Optional.ofNullable(licenceNumberTextField.getText()).ifPresent(builder::setLicenceNumber);
        Optional.ofNullable(maidenNameTextField.getText()).ifPresent(builder::setMaidenName);
        Optional.ofNullable(placeOfBirthTextField.getText()).ifPresent(builder::setPlaceOfBirth);
        Optional.ofNullable(departmentOfBirthTextField.getText()).ifPresent(builder::setDepartmentOfBirth);
        Optional.ofNullable(countryOfBirthTextField.getText()).ifPresent(builder::setCountryOfBirth);
        Optional.ofNullable(addressTextField.getText()).ifPresent(builder::setAddress);
        Optional.ofNullable(zipCodeTextField.getText()).ifPresent(builder::setZipCode);
        Optional.ofNullable(cityTextField.getText()).ifPresent(builder::setCity);
        Optional.ofNullable(emailTextField.getText()).ifPresent(builder::setEmail);
        Optional.ofNullable(phoneNumberTextField.getText()).ifPresent(builder::setPhoneNumber);
        Optional.ofNullable(licenceStateTextField.getText()).ifPresent(builder::setLicenceState);
        Optional.ofNullable(firstLicenceDatePicker.getValue()).ifPresent(builder::setFirstLicenceDate);
        Optional.ofNullable(seasonTextField.getText()).ifPresent(builder::setSeason);
        Optional.ofNullable(ageCategoryTextField.getText()).ifPresent(builder::setAgeCategory);
        Optional.ofNullable(medicalCertificateDatePicker.getValue()).ifPresent(builder::setMedicalCertificateDate);
        Optional.ofNullable(idCardDatePicker.getValue()).ifPresent(builder::setIdCardDate);

        licenseeServiceToJfxModel.saveLicensee(builder.createLicenseeJfxModel());
        clearComponents();
        closeStage();
    }

    @FXML
    private void cancel() {
        clearComponents();
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }
}
