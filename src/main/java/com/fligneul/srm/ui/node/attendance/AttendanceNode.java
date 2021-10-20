package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModelBuilder;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import com.fligneul.srm.ui.service.attendance.AttendanceServiceToJfxModel;
import com.fligneul.srm.ui.service.range.FiringPointServiceToJfxModel;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AttendanceNode extends StackPane {
    private static final String FXML_PATH = "attendance.fxml";

    @FXML
    private VBox licenseeDetailContainer;
    @FXML
    private ComboBox<FiringPointJfxModel> firingPointComboBox;
    @FXML
    private ComboBox<FiringPostJfxModel> firingPostComboBox;
    @FXML
    private Label attendanceListTitle;
    @FXML
    private Button saveButton;

    private final AtomicReference<LicenseeJfxModel> currentLicensee = new AtomicReference<>();

    private AttendanceServiceToJfxModel attendanceService;
    private AttendanceSelectionService attendanceSelectionService;

    public AttendanceNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    @Inject
    private void injectDependencies(FiringPointServiceToJfxModel firingPointService,
                                    AttendanceServiceToJfxModel attendanceService,
                                    AttendanceSelectionService attendanceSelectionService) {
        this.attendanceService = attendanceService;
        this.attendanceSelectionService = attendanceSelectionService;
        firingPointComboBox.setItems(firingPointService.getFiringPointList());
        firingPointComboBox.setConverter(new StringConverter<>() {
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

        attendanceListTitle.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        attendanceSelectionService.selectedObs()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(opt -> {
                    opt.ifPresent(currentLicensee::set);
                    licenseeDetailContainer.setVisible(opt.isPresent());
                    licenseeDetailContainer.setManaged(opt.isPresent());
                });
    }

    @FXML
    private void saveAttendance() {
        LicenseePresenceJfxModelBuilder builder = new LicenseePresenceJfxModelBuilder()
                .setLicensee(currentLicensee.get())
                .setStartDate(LocalDateTime.now())
                .setFiringPoint(firingPointComboBox.getSelectionModel().getSelectedItem());

        Optional.ofNullable(firingPostComboBox.getSelectionModel().getSelectedItem()).ifPresent(builder::setFiringPost);

        attendanceService.saveLicenseePresence(builder.createLicenseePresenceJfxModel());
    }

    @FXML
    private void clearAttendance() {
        attendanceSelectionService.clearSelected();
    }
}
