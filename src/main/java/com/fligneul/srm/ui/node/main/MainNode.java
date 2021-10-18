package com.fligneul.srm.ui.node.main;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main node of the application
 * It is a container for the applications views
 */
public class MainNode extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger(MainNode.class);
    private static final String FXML_PATH = "main.fxml";

    public MainNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

}
