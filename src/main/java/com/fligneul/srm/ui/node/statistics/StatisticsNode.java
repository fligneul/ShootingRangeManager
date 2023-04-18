package com.fligneul.srm.ui.node.statistics;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.IExportService;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.service.range.FiringPointServiceToJfxModel;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckListView;

import javax.inject.Inject;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Statistics view node
 */
public class StatisticsNode extends StackPane {
    private static final String FXML_PATH = "statistics.fxml";
    private static final Logger LOGGER = LogManager.getLogger(StatisticsNode.class);
    private static final String STATISTICS_DATE_PATTERN = "yyyy-MM-dd_HH-mm-ss";


    @FXML
    private DatePicker statisticsBeginDatePicker;
    @FXML
    private DatePicker statisticsEndDatePicker;
    @FXML
    private CheckListView<FiringPointJfxModel> rangeListView;
    @FXML
    private Button computeStatisticsButton;
    @FXML
    private VBox statisticsDisplayContainer;
    @FXML
    private Label statisticsGenerationLabel;
    @FXML
    private ProgressIndicator statisticsGenerationIndicator;

    private IExportService exportService;

    private Disposable disposable;

    public StatisticsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        computeStatisticsButton.disableProperty().bind(
                statisticsBeginDatePicker.valueProperty().isNull()
                        .or(statisticsEndDatePicker.valueProperty().isNull())
                        .or(Bindings.greaterThanOrEqual(statisticsBeginDatePicker.valueProperty().asString(), statisticsEndDatePicker.valueProperty().asString()))
                        .or(Bindings.isEmpty(rangeListView.getCheckModel().getCheckedIndices()))
        );

        rangeListView.setCellFactory(listView -> new CheckBoxListCell<>(rangeListView::getItemBooleanProperty) {
            @Override
            public void updateItem(FiringPointJfxModel firingPointJfxModel, boolean empty) {
                super.updateItem(firingPointJfxModel, empty);
                setText(firingPointJfxModel == null ? "" : firingPointJfxModel.getName());
            }
        });
    }

    /**
     * Inject GUICE dependencies
     */
    @Inject
    private void injectDependencies(final IExportService exportService, final FiringPointServiceToJfxModel firingPointServiceToJfxModel) {
        this.exportService = exportService;

        rangeListView.setItems(firingPointServiceToJfxModel.getFiringPointList());
    }

    @FXML
    private void computeStatistics() {
        LOGGER.trace("Statistics button clicked");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrement des statistiques");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName(String.format("statistics_%s.xlsx", DateTimeFormatter.ofPattern(STATISTICS_DATE_PATTERN).format(LocalDateTime.now())));
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Fichier Excel (*.xlsx)", "*.xlsx"));
        File file = fileChooser.showSaveDialog(getScene().getWindow());
        if (file != null) {

            if (disposable != null) {
                disposable.dispose();
            }
            statisticsDisplayContainer.setVisible(false);

            disposable = exportService.export(file, rangeListView.getCheckModel().getCheckedItems().stream().map(FiringPointJfxModel::getId).collect(Collectors.toList()), statisticsBeginDatePicker.getValue(), statisticsEndDatePicker.getValue())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(JavaFxScheduler.platform())
                    .doOnSubscribe(any -> {
                        statisticsGenerationLabel.setText("Génération en cours");
                        statisticsGenerationIndicator.setVisible(true);
                        statisticsGenerationIndicator.setManaged(true);
                        statisticsDisplayContainer.setVisible(true);
                    })
                    .doOnComplete(() -> {
                        statisticsGenerationLabel.setText("Génération terminée");
                        statisticsGenerationIndicator.setVisible(false);
                        statisticsGenerationIndicator.setManaged(false);
                    })
                    .doOnError(throwable -> {
                        statisticsGenerationLabel.setText("Erreur lors de la génération");
                        statisticsGenerationIndicator.setVisible(false);
                        statisticsGenerationIndicator.setManaged(false);
                    })
                    .subscribe(path -> {
                        LOGGER.info("Export complete, {}", path);
                        openFile(path);
                    }, e -> LOGGER.error("An error occurred during export", e));
        } else {
            LOGGER.warn("No file selected, export cancelled");
        }
    }

    /**
     * Open the given file with the OS
     *
     * @param path
     *         file path
     * @throws IOException
     *         error during opening
     */
    private void openFile(Path path) throws IOException {
        File file = new File(path.toUri());

        if (!Desktop.isDesktopSupported()) {
            LOGGER.error("Desktop is not supported");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) {
            desktop.open(file);
        }
    }
}
