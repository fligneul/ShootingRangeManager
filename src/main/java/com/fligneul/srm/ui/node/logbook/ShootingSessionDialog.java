package com.fligneul.srm.ui.node.logbook;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.ValidatedDatePicker;
import com.fligneul.srm.ui.component.ValidatedTextField;
import com.fligneul.srm.ui.component.ValidationUtils;
import com.fligneul.srm.ui.converter.WeaponConverter;
import com.fligneul.srm.ui.model.logbook.ShootingLogbookJfxModel;
import com.fligneul.srm.ui.model.logbook.ShootingSessionJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.service.weapon.WeaponServiceToJfxModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Node for registering a shooting session
 */
public class ShootingSessionDialog extends GridPane {
    private static final Logger LOGGER = LogManager.getLogger(ShootingSessionDialog.class);
    private static final String FXML_PATH = "shootingSessionDialog.fxml";

    @FXML
    private ValidatedDatePicker sessionDatePicker;
    @FXML
    private ValidatedTextField<String> instructorNameTextField;
    @FXML
    private ComboBox<WeaponJfxModel> weaponComboBox;
    @FXML
    private Button saveButton;

    private final ShootingLogbookJfxModel shootingLogbookJfxModel;

    public ShootingSessionDialog(ShootingLogbookJfxModel shootingLogbookJfxModel) {
        this.shootingLogbookJfxModel = shootingLogbookJfxModel;
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        instructorNameTextField.setValidator(ValidationUtils.validateRequiredString());
        saveButton.disableProperty().bind(instructorNameTextField.isValidProperty().not().or(sessionDatePicker.isValidProperty().not())
                .or(Bindings.createBooleanBinding(() -> weaponComboBox.isDisable() || weaponComboBox.getSelectionModel().getSelectedItem() != null, weaponComboBox.disableProperty(), weaponComboBox.getSelectionModel().selectedIndexProperty()).not()));
    }

    @Inject
    private void injectDependencies(WeaponServiceToJfxModel weaponService) {
        weaponComboBox.setItems(weaponService.getWeaponList());
        weaponComboBox.setConverter(new WeaponConverter());
    }

    @FXML
    private void personalWeapon() {
        weaponComboBox.setDisable(true);
        weaponComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void clubWeapon() {
        weaponComboBox.setDisable(false);
    }

    @FXML
    private void saveSession() {
        final ShootingSessionJfxModel shootingSessionJfxModel = new ShootingSessionJfxModel(sessionDatePicker.getValue(), instructorNameTextField.getText());
        Optional.ofNullable(weaponComboBox.getSelectionModel().getSelectedItem()).ifPresent(shootingSessionJfxModel::setWeapon);

        shootingLogbookJfxModel.getSessions().add(shootingSessionJfxModel);
        closeStage();
    }

    @FXML
    private void cancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }

}
