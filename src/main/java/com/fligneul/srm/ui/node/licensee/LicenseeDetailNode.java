package com.fligneul.srm.ui.node.licensee;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.node.utils.FormatterUtils;
import com.fligneul.srm.ui.service.licensee.LicenseeSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

public class LicenseeDetailNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(LicenseeDetailNode.class);
    private static final String FXML_PATH = "licenseeDetail.fxml";

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
    private TextField dateOfBirthTextField;
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
    private TextField medicalCertificateTextField;
    @FXML
    private TextField idCardTextField;
    @FXML
    private CheckBox idPhotoCheckBox;

    private LicenseeServiceToJfxModel licenseeServiceToJfxModel;
    private LicenseeSelectionService licenseeSelectionService;

    private LicenseeJfxModel currentLicenseeJfxModel;

    public LicenseeDetailNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        Optional.ofNullable(getClass().getResourceAsStream("/blank.png"))
                .ifPresent(is -> this.profileImage.setImage(new Image(is)));
    }

    /**
     * Inject GUICE dependencies
     *
     * @param licenseeServiceToJfxModel
     *         service to jfx model for licensee
     * @param licenseeSelectionService
     *         service for the current selected licensee
     */
    @Inject
    public void injectDependencies(final LicenseeServiceToJfxModel licenseeServiceToJfxModel,
                                   final LicenseeSelectionService licenseeSelectionService) {
        this.licenseeServiceToJfxModel = licenseeServiceToJfxModel;
        this.licenseeSelectionService = licenseeSelectionService;

        licenseeSelectionService.selectedObs()
                .distinctUntilChanged()
                .observeOn(JavaFxScheduler.platform())
                .doOnNext(s -> LOGGER.debug("Select licensee " + s.map(LicenseeJfxModel::getLicenceNumber).orElse("null")))
                .doOnNext(s -> clearComponents())
                .subscribe(optLicensee -> optLicensee.ifPresent(this::updateComponents), LOGGER::error);
    }

    private void clearComponents() {
        currentLicenseeJfxModel = null;
        setVisible(false);

        licenceNumberTextField.textProperty().unbind();
        firstnameTextField.textProperty().unbind();
        lastnameTextField.textProperty().unbind();
        dateOfBirthTextField.textProperty().unbind();
        maidenNameTextField.textProperty().unbind();
        placeOfBirthTextField.textProperty().unbind();
        departmentOfBirthTextField.textProperty().unbind();
        countryOfBirthTextField.textProperty().unbind();
        addressTextField.textProperty().unbind();
        zipCodeTextField.textProperty().unbind();
        cityTextField.textProperty().unbind();
        emailTextField.textProperty().unbind();
        phoneNumberTextField.textProperty().unbind();
        licenceStateTextField.textProperty().unbind();
        firstLicenceDateTextField.textProperty().unbind();
        seasonTextField.textProperty().unbind();
        ageCategoryTextField.textProperty().unbind();
        medicalCertificateTextField.textProperty().unbind();
        idCardTextField.textProperty().unbind();
        handisportCheckBox.selectedProperty().unbind();
        idPhotoCheckBox.selectedProperty().unbind();
        licenceBlacklistLabel.managedProperty().unbind();
        licenceBlacklistLabel.visibleProperty().unbind();

        licenceNumberTextField.setText(EMPTY);
        firstnameTextField.setText(EMPTY);
        lastnameTextField.setText(EMPTY);
        dateOfBirthTextField.setText(EMPTY);
        maidenNameTextField.setText(EMPTY);
        placeOfBirthTextField.setText(EMPTY);
        departmentOfBirthTextField.setText(EMPTY);
        countryOfBirthTextField.setText(EMPTY);
        addressTextField.setText(EMPTY);
        zipCodeTextField.setText(EMPTY);
        cityTextField.setText(EMPTY);
        emailTextField.setText(EMPTY);
        phoneNumberTextField.setText(EMPTY);
        licenceStateTextField.setText(EMPTY);
        firstLicenceDateTextField.setText(EMPTY);
        seasonTextField.setText(EMPTY);
        ageCategoryTextField.setText(EMPTY);
        medicalCertificateTextField.setText(EMPTY);
        idCardTextField.setText(EMPTY);
        handisportCheckBox.setSelected(false);
        idPhotoCheckBox.setSelected(false);
        licenceBlacklistLabel.setManaged(false);
        licenceBlacklistLabel.setVisible(false);
    }

    private void updateComponents(final LicenseeJfxModel licenseeJfxModel) {
        currentLicenseeJfxModel = licenseeJfxModel;
        setVisible(true);

        licenceNumberTextField.textProperty().bind(licenseeJfxModel.licenceNumberProperty());
        firstnameTextField.textProperty().bind(licenseeJfxModel.firstNameProperty());
        lastnameTextField.textProperty().bind(licenseeJfxModel.lastNameProperty());
        dateOfBirthTextField.textProperty().bind(Bindings.createStringBinding(() -> FormatterUtils.formatDate(licenseeJfxModel.getDateOfBirth()), licenseeJfxModel.dateOfBirthProperty()));
        maidenNameTextField.textProperty().bind(licenseeJfxModel.maidenNameProperty());
        placeOfBirthTextField.textProperty().bind(licenseeJfxModel.placeOfBirthProperty());
        departmentOfBirthTextField.textProperty().bind(licenseeJfxModel.departmentOfBirthProperty());
        countryOfBirthTextField.textProperty().bind(licenseeJfxModel.countryOfBirthProperty());
        addressTextField.textProperty().bind(licenseeJfxModel.addressProperty());
        zipCodeTextField.textProperty().bind(licenseeJfxModel.zipCodeProperty());
        cityTextField.textProperty().bind(licenseeJfxModel.cityProperty());
        emailTextField.textProperty().bind(licenseeJfxModel.emailProperty());
        phoneNumberTextField.textProperty().bind(licenseeJfxModel.phoneNumberProperty());
        licenceStateTextField.textProperty().bind(licenseeJfxModel.licenceStateProperty());
        firstLicenceDateTextField.textProperty().bind(Bindings.createStringBinding(() -> FormatterUtils.formatDate(licenseeJfxModel.getFirstLicenceDate()), licenseeJfxModel.firstLicenceDateProperty()));
        seasonTextField.textProperty().bind(licenseeJfxModel.seasonProperty());
        ageCategoryTextField.textProperty().bind(licenseeJfxModel.ageCategoryProperty());
        handisportCheckBox.selectedProperty().bind(licenseeJfxModel.handisportProperty());
        medicalCertificateTextField.textProperty().bind(Bindings.createStringBinding(() -> FormatterUtils.formatDate(licenseeJfxModel.getMedicalCertificateDate()), licenseeJfxModel.medicalCertificateDateProperty()));
        idCardTextField.textProperty().bind(Bindings.createStringBinding(() -> FormatterUtils.formatDate(licenseeJfxModel.getIdCardDate()), licenseeJfxModel.idCardDateProperty()));
        handisportCheckBox.selectedProperty().bind(licenseeJfxModel.handisportProperty());
        idPhotoCheckBox.selectedProperty().bind(licenseeJfxModel.idPhotoProperty());
        licenceBlacklistLabel.managedProperty().bind(licenseeJfxModel.blacklistedProperty());
        licenceBlacklistLabel.visibleProperty().bind(licenseeJfxModel.blacklistedProperty());
    }

    @FXML
    private void deleteLicensee() {
        DialogUtils.showConfirmationDialog("Suppression d'un licencié", "Supprimer un licencié",
                "Etes-vous sur de vouloir supprimer le licencié \"" + currentLicenseeJfxModel.getLastName() + " " + currentLicenseeJfxModel.getFirstName() + "\"",
                () -> {
                    licenseeServiceToJfxModel.deleteLicensee(currentLicenseeJfxModel);
                    licenseeSelectionService.setSelected(null);
                    clearComponents();
                });
    }

    @FXML
    private void editLicensee() {
        DialogUtils.showCustomDialog("Modification d'un licencié", new LicenseeCreateNode(currentLicenseeJfxModel));
    }
}
