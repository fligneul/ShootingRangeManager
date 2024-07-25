package com.fligneul.srm.ui.node.settings.dialog;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.converter.FiringPointConverter;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModelBuilder;
import com.fligneul.srm.ui.service.range.FiringPointServiceToJfxModel;
import com.fligneul.srm.ui.service.weapon.WeaponServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckComboBox;

import javax.inject.Inject;
import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * Weapon configuration dialog
 */
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
    @FXML
    private CheckComboBox<FiringPointJfxModel> firingPointCheckComboBox;

    private WeaponServiceToJfxModel weaponService;
    private FiringPointServiceToJfxModel firingPointService;
    private WeaponJfxModel currentWeaponJfxModel;

    public WeaponDialog(final WeaponJfxModel weaponJfxModel) {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);


        buyDatePicker.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                buyDatePicker.setValue(buyDatePicker.getConverter().fromString(buyDatePicker.getEditor().getText()));
            }
        });

        firingPointCheckComboBox.setConverter(new FiringPointConverter());
        firingPointCheckComboBox.getItems().setAll(firingPointService.getFiringPointList());

        Optional.ofNullable(weaponJfxModel).ifPresent(this::updateComponents);
    }

    public WeaponDialog() {
        this(null);
    }

    /**
     * Inject GUICE dependencies
     *
     * @param weaponService
     *         weapon jfx service
     * @param firingPointService
     *         firing point jfx service
     */
    @Inject
    public void injectDependencies(final WeaponServiceToJfxModel weaponService, final FiringPointServiceToJfxModel firingPointService) {
        this.weaponService = weaponService;
        this.firingPointService = firingPointService;
    }

    private void clearComponents() {
        currentWeaponJfxModel = null;

        nameTextField.setText(EMPTY);
        identificationNumberTextField.setText(EMPTY);
        caliberTextField.setText(EMPTY);
        buyDatePicker.getEditor().setText(EMPTY);
    }

    private void updateComponents(WeaponJfxModel weaponJfxModel) {
        currentWeaponJfxModel = weaponJfxModel;

        nameTextField.setText(weaponJfxModel.getName());
        identificationNumberTextField.setText(String.valueOf(weaponJfxModel.getIdentificationNumber()));
        caliberTextField.setText(weaponJfxModel.getCaliber());
        buyDatePicker.setValue(weaponJfxModel.getBuyDate());

        weaponJfxModel.availableFiringPointProperty().forEach(model -> firingPointCheckComboBox.getCheckModel().check(firingPointCheckComboBox.getItems().stream().filter(fpModel -> fpModel.getId() == model.getId()).findFirst().orElseThrow()));
    }

    @FXML
    private void saveWeapon() {
        final WeaponJfxModelBuilder builder = new WeaponJfxModelBuilder();

        builder.setId((currentWeaponJfxModel != null ? currentWeaponJfxModel.getId() : -1))
                .setName(nameTextField.getText())
                .setIdentificationNumber(Integer.parseInt(identificationNumberTextField.getText()))
                .setCaliber(caliberTextField.getText())
                .setBuyDate(buyDatePicker.getValue())
                .setAvailableFiringPoint(firingPointCheckComboBox.getCheckModel().getCheckedItems());

        weaponService.saveWeapon(builder.createWeaponJfxModel());
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
