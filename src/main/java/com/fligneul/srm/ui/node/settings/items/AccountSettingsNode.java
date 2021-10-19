package com.fligneul.srm.ui.node.settings.items;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.UserService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import javax.inject.Inject;

public class AccountSettingsNode extends StackPane implements ISettingsItemNode {
    private static final String FXML_PATH = "accountSettings.fxml";
    private static final String TITLE = "Mon compte";

    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;
    @FXML
    private Label infoLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Button saveButton;

    private UserService userService;

    public AccountSettingsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        saveButton.disableProperty().bind(
                Bindings.createBooleanBinding(() -> passwordField.getText().isBlank() || passwordConfirmField.getText().isBlank(),
                        passwordField.textProperty(),
                        passwordConfirmField.textProperty()));

        passwordConfirmField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (KeyCode.ENTER.equals(event.getCode())) {
                saveButton.fire();
            }
        });

    }

    @Inject
    private void injectDependencies(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void save() {
        clearInfo();
        clearError();
        if (!passwordField.getText().equals(passwordConfirmField.getText())) {
            displayError("Les deux mots de passe sont différents");
        } else {
            if (!userService.updatePassword(passwordField.getText().toCharArray())) {
                clearInfo();
                displayError("Erreur lors de la mise à jour du mot de passe");
            } else {
                clearError();
                displayInfo("Mot de passe mis à jour");
                passwordField.setText("");
                passwordConfirmField.setText("");
            }
        }
    }

    private void clearInfo() {
        infoLabel.setManaged(false);
        infoLabel.setText("");
    }

    private void clearError() {
        errorLabel.setManaged(false);
        errorLabel.setText("");
    }

    private void displayInfo(final String infoMessage) {
        infoLabel.setText(infoMessage);
        infoLabel.setManaged(true);
    }

    private void displayError(final String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setManaged(true);
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
