package com.fligneul.srm.ui.node.history;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.cell.table.ButtonActionTableCell;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.node.utils.FormatterUtils;
import com.fligneul.srm.ui.service.history.HistoryAttendanceServiceToJfxModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.COLOR_GREY;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.COLOR_RED;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EDIT_FA_ICON;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.TRASH_FA_ICON;

/**
 * Presence table view for history view
 */
public class HistoryTableView extends TableView<LicenseePresenceJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(HistoryTableView.class);
    private static final String FXML_PATH = "historyTableView.fxml";

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
    private TableColumn<LicenseePresenceJfxModel, LicenseePresenceJfxModel> editColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, LicenseePresenceJfxModel> deleteColumn;
    private LocalDate date;

    public HistoryTableView() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    /**
     * Inject GUICE dependencies
     *
     * @param attendanceService
     *         service to jfx model for history attendance
     */
    @Inject
    private void injectDependencies(HistoryAttendanceServiceToJfxModel attendanceService) {
        licenseeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(FormatterUtils.formatLicenseeName(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getLicensee())));
        startTimeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStartDate().format(DateTimeFormatter.ofPattern("HH:mm"))));
        firingPointColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getFiringPoint().getName()));
        firingPostColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getFiringPost()).map(FiringPostJfxModel::getName).orElse("-")));
        weaponColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getWeapon()).map(WeaponJfxModel::getName).orElse("-")));
        stopTimeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStopDate()).map(date -> date.format(DateTimeFormatter.ofPattern("HH:mm"))).orElse("-")));
        editColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue()));
        deleteColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue()));

        editColumn.setCellFactory(param -> new ButtonActionTableCell<>(EDIT_FA_ICON, COLOR_GREY, item -> {
            LOGGER.info("Edit licensee presence {}", item.getId());
            DialogUtils.showCustomDialog("Modification d'un enregistrement de présence", new HistoryEditNode(date, item));
        }));

        deleteColumn.setCellFactory(param -> new ButtonActionTableCell<>(TRASH_FA_ICON, COLOR_RED, item -> {
            DialogUtils.showConfirmationDialog("Suppression d'un enregistrement", "Supprimer un enregistrement",
                    "Etes-vous sur de vouloir supprimer l'enregistrement sélectionné ?",
                    () -> {
                        attendanceService.deleteLicenseePresence(item);
                    });
        }));
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
