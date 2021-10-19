package com.fligneul.srm.ui.node.settings.items;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.UserService;
import com.fligneul.srm.ui.model.user.UserJfxModel;
import com.fligneul.srm.ui.node.settings.dialog.UserDialog;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.node.utils.ListViewUtils;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import javax.inject.Inject;

public class UsersSettingsNode extends StackPane implements ISettingsItemNode {
    private static final String FXML_PATH = "usersSettings.fxml";
    private static final String TITLE = "Utilisateurs";

    @FXML
    private TextField shootingRangeNameTextField;
    @FXML
    private ListView<UserJfxModel> userListView;
    @FXML
    private Button userDeleteButton;
    private UserService userService;

    public UsersSettingsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        userListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<UserJfxModel> call(ListView<UserJfxModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(UserJfxModel item, boolean empty) {
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
        ListViewUtils.addClearOnEmptySelection(userListView);
        userDeleteButton.disableProperty().bind(userListView.getSelectionModel().selectedItemProperty().isNull());

    }

    @Inject
    private void injectDependencies(UserService userService) {
        this.userService = userService;

        userService.getUserObs()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(users -> userListView.setItems(FXCollections.observableList(users)));
    }

    @FXML
    private void createUser() {
        DialogUtils.showCustomDialog("Création utilisateur", new UserDialog());
    }

    @FXML
    private void deleteUser() {
        DialogUtils.showConfirmationDialog("Suppression d'un utilisateur", "Supprimer un utilisateur",
                "Etes-vous sur de vouloir supprimer l'utilisateur \"" + userListView.getSelectionModel().getSelectedItem().getName() + "\"",
                () -> userService.deleteUser(userListView.getSelectionModel().getSelectedItem().getName()));
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
