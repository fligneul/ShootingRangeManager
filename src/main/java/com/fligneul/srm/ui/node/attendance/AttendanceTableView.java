package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.cell.ButtonActionTableCell;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.node.utils.FormatterUtils;
import com.fligneul.srm.ui.service.attendance.AttendanceServiceToJfxModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.COLOR_GREY;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.DOOR_FA_ICON;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY_HYPHEN;

/**
 * Attendance tableview node
 * Display all attendance for the current day
 */
public class AttendanceTableView extends TableView<LicenseePresenceJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceTableView.class);
    private static final String FXML_PATH = "attendanceTableView.fxml";

    @FXML
    private TableColumn<LicenseePresenceJfxModel, String> licenseeColumn;
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

    /**
     * Create the node and load the associated FXML file
     */
    public AttendanceTableView() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    /**
     * Inject GUICE dependencies
     *
     * @param attendanceService
     *         selection service for the current licensee
     */
    @Inject
    public void injectDependencies(final AttendanceServiceToJfxModel attendanceService) {
        setItems(attendanceService.getLicenseePresenceList());

        licenseeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(FormatterUtils.formatLicenseeName(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getLicensee())));
        startTimeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(FormatterUtils.formatTime(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStartDate())));
        firingPointColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getFiringPoint().getName()));
        firingPostColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getFiringPost()).map(FiringPostJfxModel::getName).orElse(EMPTY_HYPHEN)));
        weaponColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getWeapon()).map(WeaponJfxModel::getName).orElse(EMPTY_HYPHEN)));
        stopTimeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(FormatterUtils.formatTime(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStopDate(), EMPTY_HYPHEN)));
        exitColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue()));

        exitColumn.setCellFactory(param -> new ButtonActionTableCell<>(DOOR_FA_ICON, COLOR_GREY, item -> {
            LOGGER.info("Licensee {} exit", item.getLicensee().getLicenceNumber());
            attendanceService.recordLicenseeExit(item);
        }, item -> item.getStopDate() != null));
    }
}
