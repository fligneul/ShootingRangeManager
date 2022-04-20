package com.fligneul.srm.ui.node.logbook;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.logbook.ShootingLogbookJfxModel;
import com.fligneul.srm.ui.model.logbook.ShootingSessionJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.service.weapon.WeaponServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

public class ShootingSessionDialog extends GridPane {
    private static final Logger LOGGER = LogManager.getLogger(ShootingSessionDialog.class);
    private static final String FXML_PATH = "shootingSessionDialog.fxml";

    @FXML
    private DatePicker sessionDatePicker;
    @FXML
    private TextField instructorNameTextField;
    @FXML
    private ComboBox<WeaponJfxModel> weaponComboBox;

    private final ShootingLogbookJfxModel shootingLogbookJfxModel;

    public ShootingSessionDialog(ShootingLogbookJfxModel shootingLogbookJfxModel) {
        this.shootingLogbookJfxModel = shootingLogbookJfxModel;
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        sessionDatePicker.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                sessionDatePicker.setValue(sessionDatePicker.getConverter().fromString(sessionDatePicker.getEditor().getText()));
            }
        });
    }

    @Inject
    private void injectDependencies(WeaponServiceToJfxModel weaponService) {

        weaponComboBox.setItems(weaponService.getWeaponList());
        weaponComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(WeaponJfxModel weaponJfxModel) {
                return Optional.ofNullable(weaponJfxModel).map(model -> model.getIdentificationNumber() + " - " + model.getName()).orElse("");
            }

            @Override
            public WeaponJfxModel fromString(String s) {
                throw new IllegalArgumentException("Should not pass here");
            }
        });
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
        if (sessionDatePicker.getValue() != null && !instructorNameTextField.getText().isBlank() && (weaponComboBox.isDisable() || weaponComboBox.getSelectionModel().getSelectedItem() != null)) {
            final ShootingSessionJfxModel shootingSessionJfxModel = new ShootingSessionJfxModel(sessionDatePicker.getValue(), instructorNameTextField.getText());
            Optional.ofNullable(weaponComboBox.getSelectionModel().getSelectedItem()).ifPresent(shootingSessionJfxModel::setWeapon);

            shootingLogbookJfxModel.getSessions().add(shootingSessionJfxModel);
            closeStage();
        }
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
