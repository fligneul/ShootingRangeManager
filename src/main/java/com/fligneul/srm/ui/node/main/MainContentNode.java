package com.fligneul.srm.ui.node.main;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.AuthenticationService;
import com.fligneul.srm.ui.node.licensee.LicenseeNode;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

    @FXML
    private TabPane mainTabPane;

    private AuthenticationService authenticationService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainContentNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if (nv != null) {
                LOGGER.info("Display tab {}", nv.getId());
            }
        });

        mainTabPane.getTabs().clear();
        mainTabPane.getTabs().add(createTab("Licenciés", new LicenseeNode()));
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

    private Tab createTab(String name, Node content) {
        Tab tab = new Tab(name);
        tab.setId(content.getClass().getSimpleName());
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }
}
