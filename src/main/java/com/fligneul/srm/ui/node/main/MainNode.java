package com.fligneul.srm.ui.node.main;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main node of the application
 * It is a container for the applications views
 */
public class MainNode extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger(MainNode.class);


    public MainNode() {
        getChildren().add(new Label("TestView"));
    }
}
