package com.fligneul.srm.ui.node.utils;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class ListViewUtils {

    public static void addClearOnEmptySelection(ListView<?> listView){
        listView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getTarget() instanceof ListCell<?> && ((ListCell<?>) event.getTarget()).getItem() == null) {
                listView.getSelectionModel().clearSelection();
            }
        });
    }
}
