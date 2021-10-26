package com.fligneul.srm.ui.node.settings.items;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.node.settings.dialog.WeaponDialog;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.node.utils.ListViewUtils;
import com.fligneul.srm.ui.service.weapon.WeaponServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

    private WeaponServiceToJfxModel weaponServiceToJfxModel;
    private final AtomicReference<WeaponJfxModel> selectedWeaponJfx = new AtomicReference<>();

    public WeaponSettingsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    @Inject
    private void injectDependencies(WeaponServiceToJfxModel weaponServiceToJfxModel) {
        this.weaponServiceToJfxModel = weaponServiceToJfxModel;

        weaponListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<WeaponJfxModel> call(ListView<WeaponJfxModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(WeaponJfxModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("");
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });

        weaponListView.setItems(weaponServiceToJfxModel.getWeaponList());
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
                () -> weaponServiceToJfxModel.deleteWeapon(selectedWeaponJfx.get()));
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
