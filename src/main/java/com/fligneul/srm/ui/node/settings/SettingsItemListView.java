package com.fligneul.srm.ui.node.settings;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.node.settings.items.ISettingsItemNode;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SettingsItemListView extends ListView<ISettingsItemNode> {
    private static final String FXML_PATH = "settingsItemListView.fxml";

    public SettingsItemListView() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        setCellFactory(new Callback<>() {
            @Override
            public ListCell<ISettingsItemNode> call(ListView<ISettingsItemNode> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ISettingsItemNode item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("");
                        } else {
                            setText(item.getTitle());
                        }
                    }
                };
            }
        });
    }
}
