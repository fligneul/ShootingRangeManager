package com.fligneul.srm.ui.node.history;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.service.history.HistoryAttendanceServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.DATE_FORMATTER;

public class HistoryNode extends StackPane {
    private static final String FXML_PATH = "history.fxml";

    @FXML
    private DatePicker historyDatePicker;
    @FXML
    private Button displayHistoryButton;
    @FXML
    private VBox historyDisplayContainer;
    @FXML
    private Label historyDate;
    @FXML
    private HistoryTableView historyTableView;
    private HistoryAttendanceServiceToJfxModel historyAttendanceServiceToJfxModel;

    public HistoryNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        displayHistoryButton.disableProperty().bind(historyDatePicker.valueProperty().isNull());
    }

    @Inject
    private void injectDependencies(final HistoryAttendanceServiceToJfxModel historyAttendanceServiceToJfxModel) {
        this.historyAttendanceServiceToJfxModel = historyAttendanceServiceToJfxModel;
    }

    @FXML
    private void displayHistory() {
        historyAttendanceServiceToJfxModel.setHistoryDate(historyDatePicker.getValue());
        historyTableView.setItems(historyAttendanceServiceToJfxModel.getLicenseePresenceList());
        historyDate.setText(historyDatePicker.getValue().format(DATE_FORMATTER));
        historyTableView.setDate(historyDatePicker.getValue());
        historyDisplayContainer.setVisible(true);
    }

    @FXML
    private void createLicenseePresence() {
        DialogUtils.showCustomDialog("Modification d'un enregistrement de présence", new HistoryEditNode(historyDatePicker.getValue()));
    }


}
