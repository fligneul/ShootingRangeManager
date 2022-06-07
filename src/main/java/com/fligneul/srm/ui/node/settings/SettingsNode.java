package com.fligneul.srm.ui.node.settings;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.node.settings.items.ISettingsItemNode;
import com.fligneul.srm.ui.service.user.UserViewService;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Setting node
 * Display a listview of all available setting categories and the setting detail on click
 */
public class SettingsNode extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger(SettingsNode.class);

    private static final String FXML_PATH = "settings.fxml";

    @FXML
    private SettingsItemListView settingsItemListView;
    @FXML
    private BorderPane settingsDetailNode;

    public SettingsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        settingsItemListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                settingsDetailNode.setCenter(newV.getNode());
            }
        });
    }

    /**
     * Inject GUICE dependencies
     *
     * @param injector
     *         GUICE injector
     * @param userViewService
     *         view available for current user
     */
    @Inject
    public void injectDependencies(final Injector injector,
                                   final UserViewService userViewService) {
        userViewService.getAccessibleSettingsNode()
                .subscribe(nodes -> {
                    ObservableList<ISettingsItemNode> settingsItemNodes = FXCollections.observableArrayList();
                    nodes.forEach(nodeClass -> {
                        try {
                            settingsItemNodes.add(injector.getInstance(nodeClass));
                        } catch (ConfigurationException | ProvisionException e) {
                            LOGGER.error("Error during node {} creation", nodeClass.getName(), e);
                        }
                    });
                    settingsItemListView.setItems(settingsItemNodes);
                });
    }
}
