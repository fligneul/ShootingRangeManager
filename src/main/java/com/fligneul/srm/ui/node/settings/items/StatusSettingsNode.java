package com.fligneul.srm.ui.node.settings.items;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.node.settings.dialog.StatusDialog;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.node.utils.ListViewUtils;
import com.fligneul.srm.ui.service.status.StatusServiceToJfxModel;
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

public class StatusSettingsNode extends StackPane implements ISettingsItemNode {
    private static final Logger LOGGER = LogManager.getLogger(StatusSettingsNode.class);

    private static final String FXML_PATH = "statusSettings.fxml";
    private static final String TITLE = "Statut";

    @FXML
    private ListView<StatusJfxModel> statusListView;
    @FXML
    private Button statusEditButton;
    @FXML
    private Button statusDeleteButton;

    private StatusServiceToJfxModel statusServiceToJfxModel;
    private final AtomicReference<StatusJfxModel> selectedStatusJfx = new AtomicReference<>();

    public StatusSettingsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    @Inject
    private void injectDependencies(StatusServiceToJfxModel statusServiceToJfxModel) {
        this.statusServiceToJfxModel = statusServiceToJfxModel;

        statusListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<StatusJfxModel> call(ListView<StatusJfxModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(StatusJfxModel item, boolean empty) {
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

        statusListView.setItems(statusServiceToJfxModel.getStatusList());
        statusListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) ->
                Optional.ofNullable(newV).ifPresentOrElse(selectedStatusJfx::set, () -> selectedStatusJfx.set(null)));
        ListViewUtils.addClearOnEmptySelection(statusListView);

        statusEditButton.disableProperty().bind(statusListView.getSelectionModel().selectedItemProperty().isNull());
        statusDeleteButton.disableProperty().bind(statusListView.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    private void createStatus() {
        DialogUtils.showCustomDialog("Enregistrement statut", new StatusDialog());
    }

    @FXML
    private void editStatus() {
        DialogUtils.showCustomDialog("Modification statut", new StatusDialog(selectedStatusJfx.get()));
    }

    @FXML
    private void deleteStatus() {
        DialogUtils.showConfirmationDialog("Suppression d'un statut", "Supprimer un statut",
                "Etes-vous sur de vouloir supprimer le statut \"" + selectedStatusJfx.get().getName() + "\"",
                () -> statusServiceToJfxModel.deleteStatus(selectedStatusJfx.get()));
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
