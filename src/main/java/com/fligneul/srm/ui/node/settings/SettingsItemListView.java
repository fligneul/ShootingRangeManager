package com.fligneul.srm.ui.node.settings;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.cell.list.SimpleListCell;
import com.fligneul.srm.ui.node.settings.items.ISettingsItemNode;
import javafx.scene.control.ListView;

/**
 * Listview for all settings node that implements {@link ISettingsItemNode}
 */
public class SettingsItemListView extends ListView<ISettingsItemNode> {
    private static final String FXML_PATH = "settingsItemListView.fxml";

    public SettingsItemListView() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        setCellFactory(param -> new SimpleListCell<>(ISettingsItemNode::getTitle));
    }
}
