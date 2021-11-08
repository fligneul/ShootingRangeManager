package com.fligneul.srm.ui.node.history;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModelBuilder;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import com.fligneul.srm.ui.service.attendance.AttendanceServiceToJfxModel;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import com.fligneul.srm.ui.service.range.FiringPointServiceToJfxModel;
import com.fligneul.srm.ui.service.status.StatusServiceToJfxModel;
import com.fligneul.srm.ui.service.weapon.WeaponServiceToJfxModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class HistoryEditNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(HistoryEditNode.class);
    private static final String FXML_PATH = "historyEdit.fxml";

    @FXML
    private Button saveButton;
    @FXML
    private TextField licenseeNumberTextField;
    @FXML
    private ComboBox<FiringPointJfxModel> firingPointComboBox;
    @FXML
    private TextField stopTimeTextField;
    @FXML
    private ComboBox<FiringPostJfxModel> firingPostComboBox;
    @FXML
    private ComboBox<WeaponJfxModel> weaponComboBox;
    @FXML
    private ComboBox<StatusJfxModel> statusComboBox;
    @FXML
    private TextField startTimeTextField;


    private final LocalDate localDate;
    private AttendanceServiceToJfxModel attendanceServiceToJfxModel;
    private LicenseeServiceToJfxModel licenseeServiceToJfxModel;

    private LicenseePresenceJfxModel currentLicenseePresenceJfxModel;

    private LocalTimeStringConverter timeStringConverter = new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), DateTimeFormatter.ofPattern("HH:mm"));

    public HistoryEditNode(final LocalDate localDate, final LicenseePresenceJfxModel licenseePresenceJfxModel) {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        this.localDate = localDate;
        Optional.ofNullable(licenseePresenceJfxModel).ifPresent(this::updateComponents);

    }

    public HistoryEditNode(final LocalDate localDate) {
        this(localDate, null);
    }

    @Inject
    public void injectDependencies(final AttendanceServiceToJfxModel attendanceServiceToJfxModel,
                                   FiringPointServiceToJfxModel firingPointService,
                                   AttendanceSelectionService attendanceSelectionService,
                                   LicenseeServiceToJfxModel licenseeServiceToJfxModel,
                                   WeaponServiceToJfxModel weaponService,
                                   StatusServiceToJfxModel statusService) {
        this.attendanceServiceToJfxModel = attendanceServiceToJfxModel;
        this.licenseeServiceToJfxModel = licenseeServiceToJfxModel;

        firingPointComboBox.setItems(firingPointService.getFiringPointList());
        firingPointComboBox.setConverter(new StringConverter<FiringPointJfxModel>() {
            @Override
            public String toString(FiringPointJfxModel firingPointJfxModel) {
                return Optional.ofNullable(firingPointJfxModel).map(FiringPointJfxModel::getName).orElse("");
            }

            @Override
            public FiringPointJfxModel fromString(String s) {
                throw new IllegalArgumentException("Should not pass here");
            }
        });
        firingPointComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                firingPostComboBox.setItems(newV.getPosts());
                firingPostComboBox.setDisable(newV.getPosts().isEmpty());
                firingPostComboBox.requestFocus();
                saveButton.setDisable(false);
            } else {
                firingPostComboBox.setItems(FXCollections.emptyObservableList());
                firingPostComboBox.setDisable(true);
                saveButton.setDisable(true);
            }
        });

        firingPostComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(FiringPostJfxModel firingPostJfxModel) {
                return Optional.ofNullable(firingPostJfxModel).map(FiringPostJfxModel::getName).orElse("");
            }

            @Override
            public FiringPostJfxModel fromString(String s) {
                throw new IllegalArgumentException("Should not pass here");
            }
        });

        weaponComboBox.setItems(weaponService.getWeaponList());
        weaponComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(WeaponJfxModel weaponJfxModel) {
                return Optional.ofNullable(weaponJfxModel).map(model -> model.getIdentificationNumber() + " - " + model.getName()).orElse("");
            }

            @Override
            public WeaponJfxModel fromString(String s) {
                throw new IllegalArgumentException("Should not pass here");
            }
        });

        statusComboBox.setItems(statusService.getStatusList());
        statusComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(StatusJfxModel statusJfxModel) {
                return Optional.ofNullable(statusJfxModel).map(StatusJfxModel::getName).orElse("");
            }

            @Override
            public StatusJfxModel fromString(String s) {
                throw new IllegalArgumentException("Should not pass here");
            }
        });

    }

    private void clearComponents() {
        currentLicenseePresenceJfxModel = null;

        licenseeNumberTextField.setText("");
        firingPointComboBox.getSelectionModel().clearSelection();
        stopTimeTextField.setText("");
        firingPostComboBox.getSelectionModel().clearSelection();
        weaponComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
        startTimeTextField.setText("");
    }

    private void updateComponents(LicenseePresenceJfxModel licenseePresenceJfxModel) {
        currentLicenseePresenceJfxModel = licenseePresenceJfxModel;

        licenseeNumberTextField.setText(licenseePresenceJfxModel.getLicensee().getLicenceNumber());
        firingPointComboBox.getSelectionModel().select(licenseePresenceJfxModel.getFiringPoint());
        Optional.ofNullable(licenseePresenceJfxModel.getStopDate())
                .map(LocalDateTime::toLocalTime)
                .map(timeStringConverter::toString)
                .ifPresent(time -> stopTimeTextField.setText(time));

        firingPostComboBox.getSelectionModel().select(licenseePresenceJfxModel.getFiringPost());
        Optional.ofNullable(licenseePresenceJfxModel.getStartDate())
                .map(LocalDateTime::toLocalTime)
                .map(timeStringConverter::toString)
                .ifPresent(time -> startTimeTextField.setText(time));
        weaponComboBox.getSelectionModel().select(licenseePresenceJfxModel.getWeapon());
        statusComboBox.getSelectionModel().select(licenseePresenceJfxModel.getStatus());

    }

    @FXML
    private void saveLicenseePresence() {
        final LicenseePresenceJfxModelBuilder builder = new LicenseePresenceJfxModelBuilder();

        licenseeServiceToJfxModel.getLicenseeList().stream().filter(licenseeJfxModel -> licenseeJfxModel.getLicenceNumber().equals(licenseeNumberTextField.getText())).findFirst().ifPresent(licensee -> {
            builder.setId((currentLicenseePresenceJfxModel != null ? currentLicenseePresenceJfxModel.getId() : -1))
                    .setLicensee(licensee)
                    .setStartDate(LocalDateTime.of(localDate, timeStringConverter.fromString(startTimeTextField.getText())))
                    .setFiringPoint(firingPointComboBox.getSelectionModel().getSelectedItem());

            Optional.ofNullable(stopTimeTextField.getText()).filter(s -> !s.isBlank()).ifPresent(time -> builder.setStopDate(LocalDateTime.of(localDate, timeStringConverter.fromString(time))));
            Optional.ofNullable(firingPostComboBox.getSelectionModel().getSelectedItem()).ifPresent(builder::setFiringPost);
            Optional.ofNullable(weaponComboBox.getSelectionModel().getSelectedItem()).ifPresent(builder::setWeapon);
            Optional.ofNullable(statusComboBox.getSelectionModel().getSelectedItem()).ifPresent(builder::setStatus);

            attendanceServiceToJfxModel.saveLicenseePresence(builder.createLicenseePresenceJfxModel());

            clearComponents();
            closeStage();
        });
    }

    @FXML
    private void cancel() {
        clearComponents();
        closeStage();
    }

    @FXML
    private void delete() {
        DialogUtils.showConfirmationDialog("Suppression d'un enregistrement", "Supprimer un enregistrement",
                "Etes-vous sur de vouloir supprimer l'enregistrement sélectionné ?",
                () -> {
                    attendanceServiceToJfxModel.deleteLicenseePresence(currentLicenseePresenceJfxModel);
                    clearComponents();
                    closeStage();
                });
    }

    private void closeStage() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }
}
