package com.fligneul.srm.ui.node.licensee;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.node.utils.ListViewUtils;
import com.fligneul.srm.ui.service.licensee.LicenseeSelectionService;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import javax.inject.Inject;

public class LicenseeNode extends StackPane {
    private static final String FXML_PATH = "licensee.fxml";

    @FXML
    private LicenseeListView licenseeListView;

    @FXML
    private LicenseeDetailNode licenseeDetailNode;

    private LicenseeSelectionService licenseeSelectionService;


    public LicenseeNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    @Inject
    public void injectDependencies(final LicenseeSelectionService licenseeSelectionService) {
        this.licenseeSelectionService = licenseeSelectionService;

        ListViewUtils.addClearOnEmptySelection(licenseeListView);
    }

    @FXML
    private void createLicensee() {
        licenseeSelectionService.setSelected(null);
        DialogUtils.showCustomDialog("Création d'un licencié", new LicenseeCreateNode());
    }

}
