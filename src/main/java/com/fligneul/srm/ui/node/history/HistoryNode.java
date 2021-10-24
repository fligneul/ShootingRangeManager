package com.fligneul.srm.ui.node.history;

import com.fligneul.srm.dao.attendance.AttendanceDAO;
import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;

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
    private AttendanceDAO attendanceDAO;

    public HistoryNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        displayHistoryButton.disableProperty().bind(historyDatePicker.valueProperty().isNull());
    }

    @Inject
    private void injectDependencies(final AttendanceDAO attendanceDAO) {
        this.attendanceDAO = attendanceDAO;
    }

    @FXML
    private void displayHistory() {
        historyDate.setText(historyDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        historyTableView.setDate(historyDatePicker.getValue());
        historyTableView.setItems(FXCollections.observableList(attendanceDAO.getByDate(historyDatePicker.getValue())));
        historyTableView.setRefreshAction(() -> historyTableView.setItems(FXCollections.observableList(attendanceDAO.getByDate(historyDatePicker.getValue()))));
        historyDisplayContainer.setVisible(true);
    }

    @FXML
    private void createLicenseePresence() {
        DialogUtils.showCustomDialog("Modification d'un enregistrement de présence", new HistoryEditNode(historyDatePicker.getValue()));
        historyTableView.setItems(FXCollections.observableList(attendanceDAO.getByDate(historyDatePicker.getValue())));
    }


}
