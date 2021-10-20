package com.fligneul.srm.ui.node.settings.items;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.PreferenceService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import javax.inject.Inject;

public class GeneralSettingsNode extends StackPane implements ISettingsItemNode {
    private static final String FXML_PATH = "generalSettings.fxml";
    private static final String TITLE = "Général";

    @FXML
    private TextField shootingRangeNameTextField;
    private PreferenceService preferenceService;

    public GeneralSettingsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

    }

    @Inject
    private void injectDependencies(final PreferenceService preferenceService) {
        this.preferenceService = preferenceService;

        shootingRangeNameTextField.setText(preferenceService.getShootingRangeName());
    }

    @FXML
    private void save() {
        preferenceService.saveShootingRangeName(shootingRangeNameTextField.getText());
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public Node getNode() {
        return this;
    }
}
