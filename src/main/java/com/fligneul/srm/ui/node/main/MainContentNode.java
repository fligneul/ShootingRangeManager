package com.fligneul.srm.ui.node.main;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.AuthenticationService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Content node of the application
 * Display all views in a TabPane and a disconnection button
 */
public class MainContentNode extends AnchorPane {
    private static final Logger LOGGER = LogManager.getLogger(MainNode.class);
    private static final String FXML_PATH = "mainContent.fxml";

    private AuthenticationService authenticationService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainContentNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    @Inject
    public void injectDependencies(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @FXML
    public void disconnect() {
        LOGGER.info("Disconnect current user");
        compositeDisposable.clear();
        authenticationService.disconnect();
    }

}
