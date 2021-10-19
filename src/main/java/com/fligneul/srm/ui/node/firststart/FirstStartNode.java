package com.fligneul.srm.ui.node.firststart;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.AuthenticationService;
import com.fligneul.srm.service.FirstStartService;
import com.fligneul.srm.service.ShutdownService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Configuration node for the first start of the application
 */
public class FirstStartNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(FirstStartNode.class);
    private static final String FXML_PATH = "firstStart.fxml";

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField passwordConfirmationTextField;
    @FXML
    private Label firstStartErrorLabel;
    @FXML
    private Button validateButton;

    private AuthenticationService authenticationService;
    private FirstStartService firstStartService;
    private ShutdownService shutdownService;

    public FirstStartNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    @Inject
    public void injectDependencies(final AuthenticationService authenticationService,
                                   final FirstStartService firstStartService,
                                   final ShutdownService shutdownService) {
        this.authenticationService = authenticationService;
        this.firstStartService = firstStartService;
        this.shutdownService = shutdownService;

        validateButton.disableProperty().bind(
                Bindings.createBooleanBinding(() -> usernameTextField.getText().isBlank() ||
                                passwordTextField.getText().isBlank() || passwordConfirmationTextField.getText().isBlank(),
                        usernameTextField.textProperty(),
                        passwordTextField.textProperty(),
                        passwordConfirmationTextField.textProperty()));

        passwordConfirmationTextField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (KeyCode.ENTER.equals(event.getCode())) {
                validateButton.fire();
            }
        });
    }

    @FXML
    private void validateFirstStart() {
        clearError();
        if (!passwordTextField.getText().equals(passwordConfirmationTextField.getText())) {
            displayError("Les deux mots de passe sont différents");
        } else {
            String username = usernameTextField.getText().toUpperCase();
            if (firstStartService.createFirstUser(username, passwordTextField.getText().toCharArray())) {
                authenticationService.authenticate(username, passwordTextField.getText().toCharArray());
            } else {
                LOGGER.error("Can't create first user");
                displayError("Erreur lors de la création du compte administrateur");
            }
        }
    }

    @FXML
    private void exit() {
        LOGGER.info("Exit application");
        shutdownService.shutdown();
    }

    private void displayError(final String errorMessage) {
        firstStartErrorLabel.setText(errorMessage);
        firstStartErrorLabel.setManaged(true);
    }

    private void clearError() {
        firstStartErrorLabel.setManaged(false);
        firstStartErrorLabel.setText("");
    }
}
