package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.cell.table.ButtonActionTableCell;
import com.fligneul.srm.ui.component.cell.table.SimpleTableCell;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.range.CaliberJfxModel;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.range.TargetHolderJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.node.utils.FormatterUtils;
import com.fligneul.srm.ui.service.attendance.AttendanceServiceToJfxModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.COLOR_GREY;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.DOOR_FA_ICON;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY_HYPHEN;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.TIME_FORMATTER;

/**
 * Attendance tableview node
 * Display all attendance for the current day
 */
public class AttendanceTableView extends TableView<LicenseePresenceJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceTableView.class);
    private static final String FXML_PATH = "attendanceTableView.fxml";

    @FXML
    private TableColumn<LicenseePresenceJfxModel, LicenseeJfxModel> licenseeColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, Optional<StatusJfxModel>> statusColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, LocalDateTime> startTimeColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, FiringPointJfxModel> firingPointColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, Optional<FiringPostJfxModel>> firingPostColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, Optional<WeaponJfxModel>> weaponColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, Optional<TargetHolderJfxModel>> targetHolderColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, Optional<CaliberJfxModel>> caliberColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, Optional<LocalDateTime>> stopTimeColumn;
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
        SortedList<LicenseePresenceJfxModel> sortedList = new SortedList<>(attendanceService.getLicenseePresenceList());
        sortedList.setComparator(Comparator.comparing(LicenseePresenceJfxModel::getStartDate));
        setItems(sortedList);

        licenseeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getLicensee()));
        licenseeColumn.setCellFactory(param -> new SimpleTableCell<>(FormatterUtils::formatLicenseeName));

        statusColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStatus())));
        statusColumn.setCellFactory(param -> new SimpleTableCell<>(optStatus -> optStatus.map(StatusJfxModel::getName).orElse(EMPTY_HYPHEN)));

        startTimeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStartDate()));
        startTimeColumn.setCellFactory(param -> new SimpleTableCell<>(startTime -> startTime.format(TIME_FORMATTER)));

        firingPointColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getFiringPoint()));
        firingPointColumn.setCellFactory(param -> new SimpleTableCell<>(FiringPointJfxModel::getName));

        firingPostColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getFiringPost())));
        firingPostColumn.setCellFactory(param -> new SimpleTableCell<>(optFiringPost -> optFiringPost.map(FiringPostJfxModel::getName).orElse(EMPTY_HYPHEN)));

        weaponColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getWeapon())));
        weaponColumn.setCellFactory(param -> new SimpleTableCell<>(optWeapon -> optWeapon.map(WeaponJfxModel::getName).orElse(EMPTY_HYPHEN)));

        targetHolderColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getTargetHolder())));
        targetHolderColumn.setCellFactory(param -> new SimpleTableCell<>(optTargetHolder -> optTargetHolder.map(TargetHolderJfxModel::getName).orElse(EMPTY_HYPHEN)));

        caliberColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getCaliber())));
        caliberColumn.setCellFactory(param -> new SimpleTableCell<>(optCaliber -> optCaliber.map(CaliberJfxModel::getName).orElse(EMPTY_HYPHEN)));

        stopTimeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStopDate())));
        stopTimeColumn.setCellFactory(param -> new SimpleTableCell<>(stopTime -> stopTime.map(date -> date.format(TIME_FORMATTER)).orElse(EMPTY_HYPHEN)));

        exitColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue()));
        exitColumn.setCellFactory(param -> new ButtonActionTableCell<>(DOOR_FA_ICON, COLOR_GREY, item -> {
            LOGGER.info("Licensee {} exit", item.getLicensee().getLicenceNumber());
            attendanceService.recordLicenseeExit(item);
        }, item -> item.getStopDate() != null));
    }
}
