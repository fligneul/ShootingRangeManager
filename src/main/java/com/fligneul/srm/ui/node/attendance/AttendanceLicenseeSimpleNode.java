package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.PreferenceService;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.logbook.ShootingSessionJfxModel;
import com.fligneul.srm.ui.node.utils.FormatterUtils;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import com.fligneul.srm.ui.service.licensee.ProfilePictureService;
import com.fligneul.srm.ui.service.logbook.ShootingLogbookServiceToJfxModel;
import io.reactivex.rxjavafx.observers.JavaFxObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * Licensee simple detail node for attendance node.
 * This node only display basic information about the licensee
 */
public class AttendanceLicenseeSimpleNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceLicenseeSimpleNode.class);
    private static final String FXML_PATH = "licenseeSimple.fxml";
    private static final PseudoClass ERROR_PSEUDO_CLASS_STATE = PseudoClass.getPseudoClass("error");
    private static final PseudoClass WARNING_PSEUDO_CLASS_STATE = PseudoClass.getPseudoClass("warning");

    @FXML
    protected ImageView profileImage;
    @FXML
    protected TextField licenceNumberTextField;
    @FXML
    protected TextField firstnameTextField;
    @FXML
    protected TextField lastnameTextField;
    @FXML
    protected TextField dateOfBirthTextField;
    @FXML
    protected CheckBox handisportCheckBox;
    @FXML
    protected GridPane licenceDetailGrid;
    @FXML
    protected Label licenceBlacklistLabel;
    @FXML
    protected TextField licenceStateTextField;
    @FXML
    protected TextField firstLicenceDateTextField;
    @FXML
    protected TextField seasonTextField;
    @FXML
    protected TextField ageCategoryTextField;
    @FXML
    protected TextField medicalCertificateValidityTextField;
    @FXML
    protected Label licenceErrorLabel;
    @FXML
    private GridPane shootingLogbookPane;
    @FXML
    protected TextField shootingLogbookCreationDateTextField;
    @FXML
    protected TextField shootingLogbookLastSessionDateTextField;

    private ShootingLogbookServiceToJfxModel shootingLogbookServiceToJfxModel;
    private PreferenceService preferenceService;
    private ProfilePictureService profilePictureService;

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
     * @param shootingLogbookServiceToJfxModel
     *         service to jfx model for shooting logbook
     * @param preferenceService
     *         application preferences
     * @param profilePictureService
     *         profile picture service
     */
    @Inject
    public void injectDependencies(final AttendanceSelectionService attendanceSelectionService,
                                   final ShootingLogbookServiceToJfxModel shootingLogbookServiceToJfxModel,
                                   final PreferenceService preferenceService,
                                   final ProfilePictureService profilePictureService) {
        this.shootingLogbookServiceToJfxModel = shootingLogbookServiceToJfxModel;
        this.preferenceService = preferenceService;
        this.profilePictureService = profilePictureService;

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
        licenceStateTextField.textProperty().unbind();
        firstLicenceDateTextField.textProperty().unbind();
        seasonTextField.textProperty().unbind();
        ageCategoryTextField.textProperty().unbind();
        medicalCertificateValidityTextField.textProperty().unbind();
        handisportCheckBox.selectedProperty().unbind();
        licenceBlacklistLabel.visibleProperty().unbind();
        licenceBlacklistLabel.managedProperty().unbind();
        licenceErrorLabel.managedProperty().unbind();
        licenceErrorLabel.visibleProperty().unbind();
        shootingLogbookCreationDateTextField.textProperty().unbind();
        shootingLogbookLastSessionDateTextField.textProperty().unbind();
        profileImage.imageProperty().unbind();

        licenceNumberTextField.setText(EMPTY);
        firstnameTextField.setText(EMPTY);
        lastnameTextField.setText(EMPTY);
        dateOfBirthTextField.setText(EMPTY);
        licenceStateTextField.setText(EMPTY);
        firstLicenceDateTextField.setText(EMPTY);
        seasonTextField.setText(EMPTY);
        ageCategoryTextField.setText(EMPTY);
        medicalCertificateValidityTextField.setText(EMPTY);
        handisportCheckBox.setSelected(false);
        licenceBlacklistLabel.setVisible(false);
        licenceBlacklistLabel.setManaged(false);
        licenceErrorLabel.setVisible(false);
        licenceErrorLabel.setManaged(false);
        shootingLogbookPane.setManaged(false);
        shootingLogbookPane.setVisible(false);
        shootingLogbookCreationDateTextField.setText(EMPTY);
        shootingLogbookLastSessionDateTextField.setText(EMPTY);
        profilePictureService.getProfilePicture().ifPresent(image -> profileImage.setImage(image));
    }

    private void updateComponents(final LicenseeJfxModel licenseeJfxModel) {
        setVisible(true);

        licenceNumberTextField.textProperty().bind(licenseeJfxModel.licenceNumberProperty());
        firstnameTextField.textProperty().bind(licenseeJfxModel.firstNameProperty());
        lastnameTextField.textProperty().bind(licenseeJfxModel.lastNameProperty());
        dateOfBirthTextField.textProperty().bind(Bindings.createStringBinding(() -> FormatterUtils.formatDate(licenseeJfxModel.getDateOfBirth()), licenseeJfxModel.dateOfBirthProperty()));
        licenceStateTextField.textProperty().bind(Bindings.createStringBinding(() -> licenseeJfxModel.getLicenceState().toString(), licenseeJfxModel.licenceStateProperty()));
        firstLicenceDateTextField.textProperty().bind(Bindings.createStringBinding(() -> FormatterUtils.formatDate(licenseeJfxModel.getFirstLicenceDate()), licenseeJfxModel.firstLicenceDateProperty()));
        seasonTextField.textProperty().bind(licenseeJfxModel.seasonProperty());
        ageCategoryTextField.textProperty().bind(licenseeJfxModel.ageCategoryProperty());
        handisportCheckBox.selectedProperty().bind(licenseeJfxModel.handisportProperty());
        licenceBlacklistLabel.visibleProperty().bind(licenseeJfxModel.blacklistedProperty());
        licenceBlacklistLabel.managedProperty().bind(licenseeJfxModel.blacklistedProperty());

        medicalCertificateValidityTextField.textProperty().bind(Bindings.createStringBinding(() -> {
            if (licenseeJfxModel.getMedicalCertificateDate() == null) {
                medicalCertificateValidityTextField.pseudoClassStateChanged(WARNING_PSEUDO_CLASS_STATE, true);
                medicalCertificateValidityTextField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS_STATE, false);
                return "Absent";
            } else {
                long duration = ChronoUnit.MONTHS.between(licenseeJfxModel.getMedicalCertificateDate(), LocalDate.now());
                if (!preferenceService.getMedicalCertificateValidityInfinite() && duration >= preferenceService.getMedicalCertificateValidityAlert() && duration < preferenceService.getMedicalCertificateValidityPeriod()) {
                    medicalCertificateValidityTextField.pseudoClassStateChanged(WARNING_PSEUDO_CLASS_STATE, true);
                    medicalCertificateValidityTextField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS_STATE, false);
                    return "Bientôt expiré";
                } else if (!preferenceService.getMedicalCertificateValidityInfinite() && duration >= preferenceService.getMedicalCertificateValidityPeriod()) {
                    medicalCertificateValidityTextField.pseudoClassStateChanged(WARNING_PSEUDO_CLASS_STATE, false);
                    medicalCertificateValidityTextField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS_STATE, true);
                    return "Expiré";
                } else {
                    medicalCertificateValidityTextField.pseudoClassStateChanged(WARNING_PSEUDO_CLASS_STATE, false);
                    medicalCertificateValidityTextField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS_STATE, false);
                    return "Valide";
                }
            }
        }, licenseeJfxModel.medicalCertificateDateProperty(), JavaFxObserver.toBinding(preferenceService.medicalCertificateValidityChanged())));

        BooleanBinding licenceReceiptIncomplete = licenseeJfxModel.medicalCertificateDateProperty().isNull().or(licenseeJfxModel.idCardDateProperty().isNull()).or(licenseeJfxModel.idPhotoProperty().not());
        licenceErrorLabel.visibleProperty().bind(licenceReceiptIncomplete);
        licenceErrorLabel.managedProperty().bind(licenceReceiptIncomplete);

        shootingLogbookServiceToJfxModel.getShootingLogbookList().stream().filter(logbook -> logbook.getLicenseeId() == licenseeJfxModel.getId()).findFirst().ifPresent(shootingLogbookJfxModel -> {
            shootingLogbookPane.setManaged(true);
            shootingLogbookPane.setVisible(true);
            shootingLogbookCreationDateTextField.textProperty().bind(Bindings.createStringBinding(() -> shootingLogbookJfxModel.getCreationDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)), shootingLogbookJfxModel.creationDateProperty()));
            shootingLogbookLastSessionDateTextField.textProperty().bind(Bindings.createStringBinding(() -> shootingLogbookJfxModel.getSessions().stream().map(ShootingSessionJfxModel::getSessionDate).max(Comparator.comparing(LocalDate::toEpochDay)).map(date -> date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))).orElse(EMPTY), shootingLogbookJfxModel.sessionsProperty()));
        });

        profileImage.imageProperty().bind(Bindings.createObjectBinding(() -> profilePictureService.getProfilePicture(licenseeJfxModel.getPhotoPath()).or(() -> profilePictureService.getProfilePicture()).orElseThrow(), licenseeJfxModel.photoPathProperty()));
    }
}
