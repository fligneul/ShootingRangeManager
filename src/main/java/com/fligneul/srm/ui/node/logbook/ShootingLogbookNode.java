package com.fligneul.srm.ui.node.logbook;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.ValidatedDatePicker;
import com.fligneul.srm.ui.component.cell.table.ButtonActionTableCell;
import com.fligneul.srm.ui.model.logbook.ShootingLogbookJfxModel;
import com.fligneul.srm.ui.model.logbook.ShootingSessionJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.node.licensee.LicenseeDetailNode;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.service.logbook.ShootingLogbookServiceToJfxModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Consumer;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.COLOR_RED;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.TRASH_FA_ICON;

/**
 * Node for shooting logbook edition
 */
public class ShootingLogbookNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(LicenseeDetailNode.class);
    private static final String FXML_PATH = "shootingLogbook.fxml";

    @FXML
    private ValidatedDatePicker creationDatePicker;
    @FXML
    private DatePicker knowledgeCheckDatePicker;
    @FXML
    private Button createSessionButton;
    @FXML
    private CheckBox whiteTargetLevelCheckBox;
    @FXML
    private TableView<ShootingSessionJfxModel> shootingSessionTableView;
    @FXML
    private TableColumn<ShootingSessionJfxModel, String> shootingSessionDateColumn;
    @FXML
    private TableColumn<ShootingSessionJfxModel, String> shootingSessionInstructorNameColumn;
    @FXML
    private TableColumn<ShootingSessionJfxModel, String> shootingSessionWeaponColumn;
    @FXML
    private TableColumn<ShootingSessionJfxModel, ShootingSessionJfxModel> deleteColumn;
    @FXML
    private Button saveButton;

    private final Consumer<ShootingLogbookJfxModel> onSaveConsumer;

    private ShootingLogbookJfxModel currentShootingLogbookJfxModel;
    private ShootingLogbookServiceToJfxModel shootingLogbookServiceToJfxModel;

    public ShootingLogbookNode(final ShootingLogbookJfxModel shootingLogbookJfxModel, Consumer<ShootingLogbookJfxModel> onSaveConsumer) {
        this.onSaveConsumer = onSaveConsumer;
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        Optional.ofNullable(shootingLogbookJfxModel).ifPresent(this::updateComponents);

        saveButton.disableProperty().bind(creationDatePicker.isValidProperty().not());

        knowledgeCheckDatePicker.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                knowledgeCheckDatePicker.setValue(knowledgeCheckDatePicker.getConverter().fromString(knowledgeCheckDatePicker.getEditor().getText()));
            }
        });

        shootingSessionDateColumn.setCellValueFactory(cellDataFeatures -> new ReadOnlyObjectWrapper<>(cellDataFeatures.getValue().getSessionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        shootingSessionInstructorNameColumn.setCellValueFactory(cellDataFeatures -> new ReadOnlyObjectWrapper<>(cellDataFeatures.getValue().getInstructorName()));
        shootingSessionWeaponColumn.setCellValueFactory(cellDataFeatures -> new ReadOnlyObjectWrapper<>(cellDataFeatures.getValue().getWeapon().map(WeaponJfxModel::getName).orElse("Personnelle")));
        deleteColumn.setCellValueFactory(cellDataFeatures -> new ReadOnlyObjectWrapper<>(cellDataFeatures.getValue()));
        deleteColumn.setCellFactory(param -> new ButtonActionTableCell<>(TRASH_FA_ICON, COLOR_RED, item -> {
            DialogUtils.showConfirmationDialog("Suppression d'une séance de tir", "Supprimer une séance de tir",
                    "Etes-vous sur de vouloir supprimer la séance de tir sélectionnée ?",
                    () -> shootingLogbookServiceToJfxModel.deleteShootingSession(currentShootingLogbookJfxModel.getId(), item));
        }));

    }

    /**
     * Inject GUICE dependencies
     *
     * @param shootingLogbookServiceToJfxModel
     *         service to jfx model for shooting logbook
     */
    @Inject
    public void injectDependencies(final ShootingLogbookServiceToJfxModel shootingLogbookServiceToJfxModel) {
        this.shootingLogbookServiceToJfxModel = shootingLogbookServiceToJfxModel;
    }

    private void clearComponents() {
        currentShootingLogbookJfxModel = null;
        createSessionButton.setDisable(true);

        creationDatePicker.getEditor().setText(EMPTY);
        knowledgeCheckDatePicker.getEditor().setText(EMPTY);
        whiteTargetLevelCheckBox.setSelected(false);
        shootingSessionTableView.setItems(FXCollections.emptyObservableList());
    }

    private void updateComponents(ShootingLogbookJfxModel shootingLogbookJfxModel) {
        currentShootingLogbookJfxModel = shootingLogbookJfxModel;
        createSessionButton.setDisable(false);

        creationDatePicker.setValue(shootingLogbookJfxModel.getCreationDate());
        knowledgeCheckDatePicker.setValue(shootingLogbookJfxModel.getKnowledgeCheckDate());
        whiteTargetLevelCheckBox.setSelected(shootingLogbookJfxModel.hasWhiteTargetLevel());
        shootingSessionTableView.setItems(shootingLogbookJfxModel.getSessions());
    }

    @FXML
    private void createSession() {
        if (currentShootingLogbookJfxModel != null) {
            DialogUtils.showCustomDialog("Séance de tir", new ShootingSessionDialog(currentShootingLogbookJfxModel));
        }
    }

    @FXML
    private void saveShootingLogbook() {
        if (creationDatePicker.getValue() != null && currentShootingLogbookJfxModel != null) {
            currentShootingLogbookJfxModel.setCreationDate(creationDatePicker.getValue());
            Optional.ofNullable(knowledgeCheckDatePicker.getValue()).ifPresent(currentShootingLogbookJfxModel::setKnowledgeCheckDate);
            currentShootingLogbookJfxModel.setWhiteTargetLevel(whiteTargetLevelCheckBox.isSelected());
            onSaveConsumer.accept(currentShootingLogbookJfxModel);
            clearComponents();
            closeStage();
        }
    }

    @FXML
    private void cancel() {
        clearComponents();
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }
}
