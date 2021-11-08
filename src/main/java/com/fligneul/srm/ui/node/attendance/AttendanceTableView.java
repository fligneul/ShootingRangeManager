package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.service.attendance.AttendanceServiceToJfxModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AttendanceTableView extends TableView<LicenseePresenceJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceTableView.class);
    private static final String FXML_PATH = "attendanceTableView.fxml";

    @FXML
    private TableColumn<LicenseePresenceJfxModel, String> licenseeColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, String> statusColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, String> startTimeColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, String> firingPointColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, String> firingPostColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, String> weaponColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, String> stopTimeColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, LicenseePresenceJfxModel> exitColumn;

    public AttendanceTableView() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    @Inject
    private void injectDependencies(AttendanceServiceToJfxModel attendanceService) {

        setItems(attendanceService.getLicenseePresenceList());
        licenseeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(formatLicenseeName(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getLicensee())));
        statusColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStatus()).map(StatusJfxModel::getName).orElse("-")));
        startTimeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStartDate().format(DateTimeFormatter.ofPattern("HH:mm"))));
        firingPointColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getFiringPoint().getName()));
        firingPostColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getFiringPost()).map(FiringPostJfxModel::getName).orElse("-")));
        weaponColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getWeapon()).map(WeaponJfxModel::getName).orElse("-")));
        stopTimeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStopDate()).map(date -> date.format(DateTimeFormatter.ofPattern("HH:mm"))).orElse("-")));
        exitColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue()));

        exitColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<LicenseePresenceJfxModel, LicenseePresenceJfxModel> call(final TableColumn<LicenseePresenceJfxModel, LicenseePresenceJfxModel> param) {
                final TableCell<LicenseePresenceJfxModel, LicenseePresenceJfxModel> cell = new TableCell<>() {

                    private final Button btn = new Button();

                    {
                        btn.setMinHeight(26);
                        btn.setMaxHeight(26);
                        btn.setMinWidth(26);
                        btn.setMaxWidth(26);
                        FontIcon fontIcon = new FontIcon("fas-door-open");
                        fontIcon.setIconColor(Paint.valueOf("#757575"));
                        fontIcon.setIconSize(18);
                        btn.setGraphic(fontIcon);

                        btn.setOnAction((ActionEvent event) -> {
                            LicenseePresenceJfxModel licenseePresenceJfxModel = getTableView().getItems().get(getIndex());
                            LOGGER.info("Licensee {} exit", licenseePresenceJfxModel.getLicensee().getLicenceNumber());
                            attendanceService.recordLicenseeExit(licenseePresenceJfxModel);
                        });
                    }

                    @Override
                    public void updateItem(LicenseePresenceJfxModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                            setDisable(item.getStopDate() != null);
                        }
                    }
                };
                return cell;
            }
        });

    }

    private String formatLicenseeName(final LicenseeJfxModel licenseeJfxModel) {
        return String.format("%s %s", licenseeJfxModel.getFirstName(), licenseeJfxModel.getLastName());
    }
}
