package com.fligneul.srm.ui.node.settings.items;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.cell.list.SimpleListCell;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.node.settings.dialog.WeaponDialog;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.node.utils.ListViewUtils;
import com.fligneul.srm.ui.service.weapon.WeaponServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Weapon configuration node
 */
public class WeaponSettingsNode extends StackPane implements ISettingsItemNode {
    private static final Logger LOGGER = LogManager.getLogger(WeaponSettingsNode.class);

    private static final String FXML_PATH = "weaponSettings.fxml";
    private static final String TITLE = "Armes";

    @FXML
    private ListView<WeaponJfxModel> weaponListView;
    @FXML
    private Button weaponEditButton;
    @FXML
    private Button weaponDeleteButton;

    private WeaponServiceToJfxModel weaponService;
    private final AtomicReference<WeaponJfxModel> selectedWeaponJfx = new AtomicReference<>();

    public WeaponSettingsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    /**
     * Inject GUICE dependencies
     *
     * @param weaponService
     *         weapon jfx service
     */
    @Inject
    public void injectDependencies(final WeaponServiceToJfxModel weaponService) {
        this.weaponService = weaponService;

        weaponListView.setCellFactory(param -> new SimpleListCell<>(WeaponJfxModel::getName));


        weaponListView.setItems(weaponService.getWeaponList());
        weaponListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) ->
                Optional.ofNullable(newV).ifPresentOrElse(selectedWeaponJfx::set, () -> selectedWeaponJfx.set(null)));
        ListViewUtils.addClearOnEmptySelection(weaponListView);

        weaponEditButton.disableProperty().bind(weaponListView.getSelectionModel().selectedItemProperty().isNull());
        weaponDeleteButton.disableProperty().bind(weaponListView.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    private void createWeapon() {
        DialogUtils.showCustomDialog("Enregistrement arme", new WeaponDialog());
    }

    @FXML
    private void editWeapon() {
        DialogUtils.showCustomDialog("Modification arme", new WeaponDialog(selectedWeaponJfx.get()));
    }

    @FXML
    private void deleteWeapon() {
        DialogUtils.showConfirmationDialog("Suppression d'une arme", "Supprimer une arme",
                "Etes-vous sur de vouloir supprimer l'arme \"" + selectedWeaponJfx.get().getName() + "\"",
                () -> weaponService.deleteWeapon(selectedWeaponJfx.get()));
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public Node getNode() {
        return this;
    }
}
