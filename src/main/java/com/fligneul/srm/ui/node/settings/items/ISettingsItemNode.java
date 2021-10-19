package com.fligneul.srm.ui.node.settings.items;

import javafx.scene.Node;

/**
 * Interface for settings node
 */
public interface ISettingsItemNode {
    /**
     * The title of the setting node
     *
     * @return the title of the setting node
     */
    String getTitle();

    /**
     * The setting node
     *
     * @return the setting node
     */
    Node getNode();
}
