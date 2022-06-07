package com.fligneul.srm.ui.node.login;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.AuthenticationService;
import com.fligneul.srm.service.PreferenceService;
import com.fligneul.srm.service.ShutdownService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * User login node
 */
public class LoginNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(LoginNode.class);
    private static final String FXML_PATH = "login.fxml";

    @FXML
    private Label shootingRangeNameLabel;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel;
    @FXML
    private Button validateButton;
    @FXML
    private Button exitButton;
    @FXML
    private ProgressIndicator connectionIndicator;

    private AuthenticationService authenticationService;
    private ShutdownService shutdownService;

    public LoginNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    /**
     * Inject GUICE dependencies
     *
     * @param authenticationService
     *         user authentication service
     * @param shutdownService
     *         service graceful shutdown
     * @param preferenceService
     *         OS preference service
     */
    @Inject
    public void injectDependencies(final AuthenticationService authenticationService,
                                   final ShutdownService shutdownService,
                                   final PreferenceService preferenceService) {
        this.authenticationService = authenticationService;
        this.shutdownService = shutdownService;

        username.setOnKeyPressed(this::enterKeyHandle);
        password.setOnKeyPressed(this::enterKeyHandle);

        shootingRangeNameLabel.setText(preferenceService.getShootingRangeName());
    }

    @FXML
    private void submitLogin() {
        LOGGER.info("Send authentication request for {}", username.getText());
        clearErrorMessage();
        showConnectingIndicator(true);

        new Thread(() -> {
            authenticationService.authenticate(username.getText(), password.getText().toCharArray())
                    .ifPresentOrElse(error -> {
                        Platform.runLater(() -> {
                            showConnectingIndicator(false);
                            errorLabel.setText(error);
                            errorLabel.setVisible(true);
                            errorLabel.setManaged(true);
                        });
                    }, this::clearErrorMessage);
        }).start();
    }

    private void showConnectingIndicator(boolean isConnection) {
        connectionIndicator.setVisible(isConnection);
        connectionIndicator.setManaged(isConnection);
        validateButton.setDisable(isConnection);
        exitButton.setDisable(isConnection);

    }

    private void clearErrorMessage() {
        Platform.runLater(() -> {
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
            errorLabel.setText(EMPTY);
        });
    }

    @FXML
    private void exit() {
        LOGGER.info("Request application shutdown");
        shutdownService.shutdown();
    }

    private void enterKeyHandle(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            submitLogin();
        }
    }
}
