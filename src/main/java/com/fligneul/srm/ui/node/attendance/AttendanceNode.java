package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.converter.CaliberConverter;
import com.fligneul.srm.ui.converter.FiringPointConverter;
import com.fligneul.srm.ui.converter.FiringPostConverter;
import com.fligneul.srm.ui.converter.StatusConverter;
import com.fligneul.srm.ui.converter.TargetHolderConverter;
import com.fligneul.srm.ui.converter.WeaponConverter;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModelBuilder;
import com.fligneul.srm.ui.model.range.CaliberJfxModel;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.range.TargetHolderJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.node.utils.FormatterUtils;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import com.fligneul.srm.ui.service.attendance.AttendanceServiceToJfxModel;
import com.fligneul.srm.ui.service.range.FiringPointServiceToJfxModel;
import com.fligneul.srm.ui.service.status.StatusServiceToJfxModel;
import com.fligneul.srm.ui.service.weapon.WeaponServiceToJfxModel;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Attendance main node
 * Display the selector, basic licensee information, current day history and attendance controls
 */
public class AttendanceNode extends StackPane {
    private static final String FXML_PATH = "attendance.fxml";

    @FXML
    private VBox licenseeDetailContainer;
    @FXML
    private ComboBox<FiringPointJfxModel> firingPointComboBox;
    @FXML
    private ComboBox<FiringPostJfxModel> firingPostComboBox;
    @FXML
    private ComboBox<WeaponJfxModel> weaponComboBox;
    @FXML
    private ComboBox<StatusJfxModel> statusComboBox;
    @FXML
    private ComboBox<TargetHolderJfxModel> targetHolderComboBox;
    @FXML
    private ComboBox<CaliberJfxModel> caliberComboBox;
    @FXML
    private Label attendanceListTitle;
    @FXML
    private Button saveButton;

    private final AtomicReference<LicenseeJfxModel> currentLicensee = new AtomicReference<>();

    private FiringPointServiceToJfxModel firingPointService;
    private AttendanceServiceToJfxModel attendanceService;
    private WeaponServiceToJfxModel weaponService;
    private AttendanceSelectionService attendanceSelectionService;
    private StatusServiceToJfxModel statusService;

    /**
     * Create the node and load the associated FXML file
     */
    public AttendanceNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    /**
     * Inject GUICE dependencies
     *
     * @param attendanceSelectionService
     *         selection service for the current licensee
     * @param firingPointService
     *         firing point jfx service
     * @param attendanceService
     *         current day attendance jfx service
     * @param weaponService
     *         weapon jfx service
     * @param statusService
     *         status jfx service
     */
    @Inject
    private void injectDependencies(final AttendanceSelectionService attendanceSelectionService,
                                    final FiringPointServiceToJfxModel firingPointService,
                                    final AttendanceServiceToJfxModel attendanceService,
                                    final WeaponServiceToJfxModel weaponService,
                                    final StatusServiceToJfxModel statusService) {
        this.attendanceSelectionService = attendanceSelectionService;
        this.firingPointService = firingPointService;
        this.attendanceService = attendanceService;
        this.weaponService = weaponService;
        this.statusService = statusService;

        initFiringPointComboBox();
        initFiringPostComboBox();
        initWeaponComboBox();
        initStatusComboBox();
        initTargetHolderComboBox();
        initCaliberComboBox();

        // Display current date
        attendanceListTitle.setText(FormatterUtils.formatDate(LocalDate.now()));

        // Display detail node if a licensee is selected
        attendanceSelectionService.selectedObs()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(opt -> {
                    opt.ifPresent(currentLicensee::set);
                    licenseeDetailContainer.setVisible(opt.isPresent());
                    licenseeDetailContainer.setManaged(opt.isPresent());
                });

        // Ensure all required value are set
        saveButton.disableProperty().bind(firingPointComboBox.getSelectionModel().selectedItemProperty().isNull()
                .or(statusComboBox.getSelectionModel().selectedItemProperty().isNull()));
    }

    private void initFiringPointComboBox() {
        firingPointComboBox.setItems(firingPointService.getFiringPointList());
        firingPointComboBox.setConverter(new FiringPointConverter());
        firingPointComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                firingPostComboBox.setItems(newV.getPosts());
                firingPostComboBox.setDisable(newV.getPosts().isEmpty());
                firingPostComboBox.requestFocus();
                targetHolderComboBox.setItems(newV.getTargetHolders());
                targetHolderComboBox.setDisable(newV.getTargetHolders().isEmpty());
                caliberComboBox.setItems(newV.getCalibers());
                caliberComboBox.setDisable(newV.getCalibers().isEmpty());
                weaponComboBox.setItems(weaponService.getWeaponListForFiringPoint(newV));
                weaponComboBox.setDisable(weaponComboBox.getItems().isEmpty());
            } else {
                firingPostComboBox.setItems(FXCollections.emptyObservableList());
                firingPostComboBox.setDisable(true);
                targetHolderComboBox.setItems(FXCollections.emptyObservableList());
                targetHolderComboBox.setDisable(true);
                caliberComboBox.setItems(FXCollections.emptyObservableList());
                caliberComboBox.setDisable(true);
                weaponComboBox.setItems(FXCollections.emptyObservableList());
                weaponComboBox.setDisable(true);
            }
        });
    }

    private void initFiringPostComboBox() {
        firingPostComboBox.setConverter(new FiringPostConverter());
    }

    private void initWeaponComboBox() {
        weaponComboBox.setConverter(new WeaponConverter());
    }

    private void initStatusComboBox() {
        statusComboBox.setItems(statusService.getStatusList());
        statusComboBox.setConverter(new StatusConverter());
        statusComboBox.getSelectionModel().selectFirst();
    }

    private void initTargetHolderComboBox() {
        targetHolderComboBox.setConverter(new TargetHolderConverter());
    }

    private void initCaliberComboBox() {
        caliberComboBox.setConverter(new CaliberConverter());
    }

    @FXML
    private void saveAttendance() {
        LicenseePresenceJfxModelBuilder builder = new LicenseePresenceJfxModelBuilder()
                .setLicensee(currentLicensee.get())
                .setStartDate(LocalDateTime.now())
                .setFiringPoint(firingPointComboBox.getSelectionModel().getSelectedItem());

        Optional.ofNullable(firingPostComboBox.getSelectionModel().getSelectedItem()).ifPresent(builder::setFiringPost);
        Optional.ofNullable(weaponComboBox.getSelectionModel().getSelectedItem()).ifPresent(builder::setWeapon);
        Optional.ofNullable(statusComboBox.getSelectionModel().getSelectedItem()).ifPresent(builder::setStatus);
        Optional.ofNullable(targetHolderComboBox.getSelectionModel().getSelectedItem()).ifPresent(builder::setTargetHolder);
        Optional.ofNullable(caliberComboBox.getSelectionModel().getSelectedItem()).ifPresent(builder::setCaliber);

        attendanceService.saveLicenseePresence(builder.createLicenseePresenceJfxModel());
        attendanceSelectionService.clearSelected();
        clearUserInput();
    }

    @FXML
    private void clearAttendance() {
        attendanceSelectionService.clearSelected();
        clearUserInput();
    }

    private void clearUserInput() {
        firingPointComboBox.getSelectionModel().clearSelection();
        firingPostComboBox.getSelectionModel().clearSelection();
        weaponComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();

        statusComboBox.getSelectionModel().selectFirst();
    }
}
