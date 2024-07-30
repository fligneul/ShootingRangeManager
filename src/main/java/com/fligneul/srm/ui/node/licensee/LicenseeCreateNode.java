package com.fligneul.srm.ui.node.licensee;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.ValidatedDatePicker;
import com.fligneul.srm.ui.component.ValidatedTextField;
import com.fligneul.srm.ui.component.ValidationUtils;
import com.fligneul.srm.ui.model.licensee.ELicenceState;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import com.fligneul.srm.ui.service.licensee.ProfilePictureService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * Licensee creation node
 */
public class LicenseeCreateNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(LicenseeCreateNode.class);
    private static final String FXML_PATH = "licenseeCreate.fxml";

    @FXML
    private Button saveButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ImageView profileImage;
    @FXML
    private TextField licenceNumberTextField;
    @FXML
    private ValidatedTextField<String> firstnameTextField;
    @FXML
    private ValidatedTextField<String> lastnameTextField;
    @FXML
    private ValidatedDatePicker dateOfBirthPicker;
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
    private ComboBox<ELicenceState> licenceStateComboBox;
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
    private ProfilePictureService profilePictureService;

    private LicenseeJfxModel currentLicenseeJfxModel;
    private URI tempProfilePicture;

    public LicenseeCreateNode(final LicenseeJfxModel licenseeJfxModel) {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

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

        firstnameTextField.setValidator(ValidationUtils.validateRequiredString());
        lastnameTextField.setValidator(ValidationUtils.validateRequiredString());
        saveButton.disableProperty().bind(firstnameTextField.isValidProperty().not().or(lastnameTextField.isValidProperty().not()).or(dateOfBirthPicker.isValidProperty().not()));

        profileImage.setOnMouseClicked(mouseEvent -> {
            FileChooser pictureChooser = new FileChooser();
            pictureChooser.setTitle("Choix de la photo");
            pictureChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            pictureChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.bmp"));
            File picture = pictureChooser.showOpenDialog(getScene().getWindow());
            if (picture != null) {
                tempProfilePicture = picture.toURI();
                try (FileInputStream profileImageInputStream = new FileInputStream(new File(picture.toURI()))) {
                    profileImage.setImage(new Image(profileImageInputStream));
                } catch (IOException e) {
                    LOGGER.error("Error during profile image loading", e);
                }
            } else {
                tempProfilePicture = null;
            }
        });

        licenceStateComboBox.setItems(FXCollections.observableArrayList(ELicenceState.values()));

        Optional.ofNullable(licenseeJfxModel).ifPresent(this::updateComponents);

    }

    public LicenseeCreateNode() {
        this(null);
    }

    /**
     * Inject GUICE dependencies
     *
     * @param licenseeServiceToJfxModel
     *         service to jfx model for licensee
     * @param profilePictureService
     *         profile picture service
     */
    @Inject
    public void injectDependencies(final LicenseeServiceToJfxModel licenseeServiceToJfxModel,
                                   final ProfilePictureService profilePictureService) {
        this.licenseeServiceToJfxModel = licenseeServiceToJfxModel;
        this.profilePictureService = profilePictureService;

        profilePictureService.getProfilePicture().ifPresent(picture -> this.profileImage.setImage(picture));
    }

    private void clearComponents() {
        currentLicenseeJfxModel = null;

        licenceNumberTextField.setText(EMPTY);
        firstnameTextField.setText(EMPTY);
        lastnameTextField.setText(EMPTY);
        dateOfBirthPicker.getEditor().setText(EMPTY);
        maidenNameTextField.setText(EMPTY);
        placeOfBirthTextField.setText(EMPTY);
        departmentOfBirthTextField.setText(EMPTY);
        countryOfBirthTextField.setText(EMPTY);
        addressTextField.setText(EMPTY);
        zipCodeTextField.setText(EMPTY);
        cityTextField.setText(EMPTY);
        emailTextField.setText(EMPTY);
        phoneNumberTextField.setText(EMPTY);
        licenceStateComboBox.getSelectionModel().clearSelection();
        licenceStateComboBox.setDisable(true);
        firstLicenceDatePicker.getEditor().setText(EMPTY);
        seasonTextField.setText(EMPTY);
        ageCategoryTextField.setText(EMPTY);
        medicalCertificateDatePicker.getEditor().setText(EMPTY);
        idCardDatePicker.getEditor().setText(EMPTY);
        handisportCheckBox.setSelected(false);
        blacklistCheckBox.setSelected(false);
        idPhotoCheckBox.setSelected(false);
        profilePictureService.getProfilePicture().ifPresent(image -> profileImage.setImage(image));
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
        licenceStateComboBox.getSelectionModel().select(licenseeJfxModel.getLicenceState());
        licenceStateComboBox.setDisable(false);
        firstLicenceDatePicker.setValue(licenseeJfxModel.getFirstLicenceDate());
        seasonTextField.setText(licenseeJfxModel.getSeason());
        ageCategoryTextField.setText(licenseeJfxModel.getAgeCategory());
        medicalCertificateDatePicker.setValue(licenseeJfxModel.getMedicalCertificateDate());
        idCardDatePicker.setValue(licenseeJfxModel.getIdCardDate());
        handisportCheckBox.setSelected(licenseeJfxModel.isHandisport());
        blacklistCheckBox.setSelected(licenseeJfxModel.isBlacklisted());
        idPhotoCheckBox.setSelected(licenseeJfxModel.hasIdPhoto());
        profilePictureService.getProfilePicture(licenseeJfxModel.getPhotoPath()).ifPresent(image -> profileImage.setImage(image));
    }

    @FXML
    private void saveLicensee() {
        final LicenseeJfxModelBuilder builder = new LicenseeJfxModelBuilder();

        builder.setId((currentLicenseeJfxModel != null ? currentLicenseeJfxModel.getId() : -1))
                .setFirstName(firstnameTextField.getValidValue())
                .setLastName(lastnameTextField.getValidValue())
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
        Optional.ofNullable(licenceStateComboBox.getValue()).ifPresent(builder::setLicenceState);
        Optional.ofNullable(firstLicenceDatePicker.getValue()).ifPresent(builder::setFirstLicenceDate);
        Optional.ofNullable(seasonTextField.getText()).ifPresent(builder::setSeason);
        Optional.ofNullable(ageCategoryTextField.getText()).ifPresent(builder::setAgeCategory);
        Optional.ofNullable(medicalCertificateDatePicker.getValue()).ifPresent(builder::setMedicalCertificateDate);
        Optional.ofNullable(idCardDatePicker.getValue()).ifPresent(builder::setIdCardDate);
        Optional.ofNullable(idCardDatePicker.getValue()).ifPresent(builder::setIdCardDate);
        Optional.ofNullable(tempProfilePicture)
                .flatMap(picture -> profilePictureService.saveProfilePicture(picture, Optional.ofNullable(currentLicenseeJfxModel).map(LicenseeJfxModel::getPhotoPath)))
                .ifPresent(builder::setPhotoPath);

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
