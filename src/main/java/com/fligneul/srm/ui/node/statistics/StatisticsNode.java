package com.fligneul.srm.ui.node.statistics;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Statistics view node
 */
public class StatisticsNode extends StackPane {
    private static final String FXML_PATH = "statistics.fxml";

    @FXML
    private DatePicker statisticsBeginDatePicker;
    @FXML
    private DatePicker statisticsEndDatePicker;
    @FXML
    private Button computeStatisticsButton;
    @FXML
    private VBox statisticsDisplayContainer;

    public StatisticsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        computeStatisticsButton.disableProperty().bind(
                statisticsBeginDatePicker.valueProperty().isNull()
                        .or(statisticsEndDatePicker.valueProperty().isNull())
        );
    }

    /**
     * Inject GUICE dependencies
     */
    @Inject
    private void injectDependencies() {
        // TODO inject needed dependencies
    }

    @FXML
    private void computeStatistics() {
        try {
            // TODO Export for the selected interval
            throw new IOException();
        } catch (IOException e) {
            // TODO Display error popup
            e.printStackTrace();
        }
    }
}
