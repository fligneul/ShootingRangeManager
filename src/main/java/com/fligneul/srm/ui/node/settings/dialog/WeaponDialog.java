package com.fligneul.srm.ui.node.settings.dialog;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModelBuilder;
import com.fligneul.srm.ui.service.weapon.WeaponServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

public class WeaponDialog extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(WeaponDialog.class);
    private static final String FXML_PATH = "weaponDialog.fxml";

    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField identificationNumberTextField;
    @FXML
    private TextField caliberTextField;
    @FXML
    private DatePicker buyDatePicker;

    private WeaponServiceToJfxModel weaponServiceToJfxModel;
    private WeaponJfxModel currentWeaponJfxModel;

    public WeaponDialog(final WeaponJfxModel weaponJfxModel) {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        Optional.ofNullable(weaponJfxModel).ifPresent(this::updateComponents);

        buyDatePicker.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                buyDatePicker.setValue(buyDatePicker.getConverter().fromString(buyDatePicker.getEditor().getText()));
            }
        });
    }

    public WeaponDialog() {
        this(null);
    }

    @Inject
    public void injectDependencies(final WeaponServiceToJfxModel weaponServiceToJfxModel) {
        this.weaponServiceToJfxModel = weaponServiceToJfxModel;
    }

    private void clearComponents() {
        currentWeaponJfxModel = null;

        nameTextField.setText("");
        identificationNumberTextField.setText("");
        caliberTextField.setText("");
        buyDatePicker.getEditor().setText("");
    }

    private void updateComponents(WeaponJfxModel weaponJfxModel) {
        currentWeaponJfxModel = weaponJfxModel;

        nameTextField.setText(weaponJfxModel.getName());
        identificationNumberTextField.setText(String.valueOf(weaponJfxModel.getIdentificationNumber()));
        caliberTextField.setText(weaponJfxModel.getCaliber());
        buyDatePicker.setValue(weaponJfxModel.getBuyDate());
    }

    @FXML
    private void saveWeapon() {
        final WeaponJfxModelBuilder builder = new WeaponJfxModelBuilder();

        builder.setId((currentWeaponJfxModel != null ? currentWeaponJfxModel.getId() : -1))
                .setName(nameTextField.getText())
                .setIdentificationNumber(Integer.parseInt(identificationNumberTextField.getText()))
                .setCaliber(caliberTextField.getText())
                .setBuyDate(buyDatePicker.getValue());

        weaponServiceToJfxModel.saveWeapon(builder.createWeaponJfxModel());
        clearComponents();
        closeStage();
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
