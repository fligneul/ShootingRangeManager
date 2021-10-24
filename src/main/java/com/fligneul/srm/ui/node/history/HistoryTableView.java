package com.fligneul.srm.ui.node.history;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.node.utils.DialogUtils;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
    private TableColumn<LicenseePresenceJfxModel, String> stopTimeColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, LicenseePresenceJfxModel> editColumn;
    @FXML
    private TableColumn<LicenseePresenceJfxModel, LicenseePresenceJfxModel> deleteColumn;
    private LocalDate date;
    private Runnable action;


    public HistoryTableView() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    @Inject
    private void injectDependencies(AttendanceServiceToJfxModel attendanceService) {
        licenseeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(formatLicenseeName(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getLicensee())));
        startTimeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStartDate().format(DateTimeFormatter.ofPattern("HH:mm"))));
        firingPointColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getFiringPoint().getName()));
        firingPostColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getFiringPost()).map(FiringPostJfxModel::getName).orElse("-")));
        stopTimeColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(Optional.ofNullable(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue().getStopDate()).map(date -> date.format(DateTimeFormatter.ofPattern("HH:mm"))).orElse("-")));
        editColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue()));
        deleteColumn.setCellValueFactory(licenseePresenceLicenseeJfxModelCellDataFeatures -> new ReadOnlyObjectWrapper<>(licenseePresenceLicenseeJfxModelCellDataFeatures.getValue()));

        editColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<LicenseePresenceJfxModel, LicenseePresenceJfxModel> call(final TableColumn<LicenseePresenceJfxModel, LicenseePresenceJfxModel> param) {
                return new TableCell<>() {

                    private final Button btn = new Button();

                    {
                        btn.setMinHeight(26);
                        btn.setMaxHeight(26);
                        btn.setMinWidth(26);
                        btn.setMaxWidth(26);
                        FontIcon fontIcon = new FontIcon("fas-edit");
                        fontIcon.setIconColor(Paint.valueOf("#757575"));
                        fontIcon.setIconSize(18);
                        btn.setGraphic(fontIcon);

                        btn.setOnAction((ActionEvent event) -> {
                            LicenseePresenceJfxModel licenseePresenceJfxModel = getTableView().getItems().get(getIndex());
                            LOGGER.info("Edit licensee presence {}", licenseePresenceJfxModel.getId());
                            DialogUtils.showCustomDialog("Modification d'un enregistrement de présence", new HistoryEditNode(date, licenseePresenceJfxModel));
                            action.run();
                        });
                    }

                    @Override
                    public void updateItem(LicenseePresenceJfxModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        });

        deleteColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<LicenseePresenceJfxModel, LicenseePresenceJfxModel> call(final TableColumn<LicenseePresenceJfxModel, LicenseePresenceJfxModel> param) {
                return new TableCell<>() {

                    private final Button btn = new Button();

                    {
                        btn.setMinHeight(26);
                        btn.setMaxHeight(26);
                        btn.setMinWidth(26);
                        btn.setMaxWidth(26);
                        FontIcon fontIcon = new FontIcon("fas-trash-alt");
                        fontIcon.setIconColor(Paint.valueOf("#e53935"));
                        fontIcon.setIconSize(18);
                        btn.setGraphic(fontIcon);

                        btn.setOnAction((ActionEvent event) -> {
                            LicenseePresenceJfxModel licenseePresenceJfxModel = getTableView().getItems().get(getIndex());
                            DialogUtils.showConfirmationDialog("Suppression d'un enregistrement", "Supprimer un enregistrement",
                                    "Etes-vous sur de vouloir supprimer l'enregistrement sélectionné ?",
                                    () -> {
                                        attendanceService.deleteLicenseePresence(licenseePresenceJfxModel);
                                        action.run();
                                    });
                        });
                    }

                    @Override
                    public void updateItem(LicenseePresenceJfxModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        });
    }

    private String formatLicenseeName(final LicenseeJfxModel licenseeJfxModel) {
        return String.format("%s %s", licenseeJfxModel.getLastName(), licenseeJfxModel.getFirstName());
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setRefreshAction(Runnable action) {
        this.action = action;
    }
}
