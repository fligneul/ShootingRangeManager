package com.fligneul.srm.ui.node.settings.dialog;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class UserDialog extends GridPane {
    private static final Logger LOGGER = LogManager.getLogger(UserDialog.class);
    private static final String FXML_PATH = "userDialog.fxml";

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;
    @FXML
    private CheckBox isAdminCheckBox;
    @FXML
    private Label errorLabel;

    private UserService userService;

    public UserDialog() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    @Inject
    private void injectDependencies(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void saveUser(ActionEvent event) {
        clearError();
        if (!passwordField.getText().equals(passwordConfirmField.getText())) {
            displayError("Les deux mots de passe sont différents");
        } else {
            LOGGER.info("Register user " + usernameTextField.getText());
            userService.registerUser(usernameTextField.getText(), passwordField.getText().toCharArray(), isAdminCheckBox.isSelected());
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

    private void displayError(final String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setManaged(true);
    }

    private void clearError() {
        errorLabel.setManaged(false);
        errorLabel.setText("");
    }

}
