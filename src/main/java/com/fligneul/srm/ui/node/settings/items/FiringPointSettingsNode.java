package com.fligneul.srm.ui.node.settings.items;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.cell.list.SimpleListCell;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.node.utils.ListViewUtils;
import com.fligneul.srm.ui.service.range.FiringPointServiceToJfxModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Firing point and firing post configuration node
 */
public class FiringPointSettingsNode extends StackPane implements ISettingsItemNode {
    private static final Logger LOGGER = LogManager.getLogger(FiringPointSettingsNode.class);

    private static final String FXML_PATH = "firingPointSettings.fxml";
    private static final String TITLE = "Pas de tir";

    @FXML
    private ListView<FiringPointJfxModel> firingPointListView;
    @FXML
    private ListView<FiringPostJfxModel> firingPostListView;
    @FXML
    private Button firingPointEditButton;
    @FXML
    private Button firingPointDeleteButton;
    @FXML
    private Button firingPostCreateButton;
    @FXML
    private Button firingPostEditButton;
    @FXML
    private Button firingPostDeleteButton;

    private FiringPointServiceToJfxModel firingPointService;
    private final AtomicReference<FiringPointJfxModel> firingPointJfx = new AtomicReference<>();

    public FiringPointSettingsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    /**
     * Inject GUICE dependencies
     *
     * @param firingPointService
     *         firing point jfx service
     */
    @Inject
    public void injectDependencies(final FiringPointServiceToJfxModel firingPointService) {
        this.firingPointService = firingPointService;

        initFiringPointListView();

        firingPointEditButton.disableProperty().bind(firingPointListView.getSelectionModel().selectedItemProperty().isNull());
        firingPointDeleteButton.disableProperty().bind(firingPointListView.getSelectionModel().selectedItemProperty().isNull());

        firingPostListView.setCellFactory(param -> new SimpleListCell<>(FiringPostJfxModel::getName));
        ListViewUtils.addClearOnEmptySelection(firingPostListView);

        firingPostCreateButton.disableProperty().bind(firingPointListView.getSelectionModel().selectedItemProperty().isNull());
        firingPostEditButton.disableProperty().bind(firingPostListView.getSelectionModel().selectedItemProperty().isNull());
        firingPostDeleteButton.disableProperty().bind(firingPostListView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void initFiringPointListView() {
        firingPointListView.setCellFactory(param -> new SimpleListCell<>(FiringPointJfxModel::getName));
        firingPointListView.setItems(firingPointService.getFiringPointList());
        firingPointListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            Optional.ofNullable(newV).ifPresentOrElse(n -> {
                firingPointJfx.set(n);
                firingPostListView.setItems(n.getPosts());
                firingPostListView.setPlaceholder(new Label("Aucun poste de tir enregistré"));
            }, () -> {
                firingPointJfx.set(null);
                firingPostListView.setItems(FXCollections.emptyObservableList());
                firingPostListView.setPlaceholder(new Label("Veuillez sélectionner un pas de tir"));

            });
        });
        ListViewUtils.addClearOnEmptySelection(firingPointListView);
    }

    @FXML
    private void createFiringPoint() {
        DialogUtils.showInputTextDialog("Création d'un pas de tir", "Nouveau pas de tir", "Nom", "",
                name -> firingPointService.saveFiringPoint(new FiringPointJfxModel(name)));
    }

    @FXML
    private void editFiringPoint() {
        DialogUtils.showInputTextDialog("Modification d'un pas de tir", "Modification du pas de tir", "Nom", firingPointJfx.get().getName(),
                name -> {
                    final FiringPointJfxModel firingPointJfxModel = firingPointListView.getSelectionModel().getSelectedItem();
                    firingPointJfxModel.setName(name);
                    firingPointService.saveFiringPoint(firingPointJfxModel);
                });
    }

    @FXML
    private void deleteFiringPoint() {
        DialogUtils.showConfirmationDialog("Suppression d'un pas de tir", "Supprimer un pas de tir",
                "Etes-vous sur de vouloir supprimer le pas de tir \"" + firingPointListView.getSelectionModel().getSelectedItem().getName() + "\"",
                () -> firingPointService.deleteFiringPoint(firingPointListView.getSelectionModel().getSelectedItem()));
    }

    @FXML
    private void createFiringPost() {
        DialogUtils.showInputTextDialog("Création d'un poste de tir", "Nouveau poste de tir", "Nom", "",
                name -> firingPointService.saveFiringPost(firingPointJfx.get().getId(), new FiringPostJfxModel(name)));
    }

    @FXML
    private void editFiringPost() {
        DialogUtils.showInputTextDialog("Modification d'un poste de tir", "Modification du poste de tir", "Nom", firingPostListView.getSelectionModel().getSelectedItem().getName(),
                name -> {
                    final FiringPostJfxModel firingPostJfxModel = firingPostListView.getSelectionModel().getSelectedItem();
                    firingPostJfxModel.setName(name);
                    firingPointService.saveFiringPost(firingPointJfx.get().getId(), firingPostJfxModel);
                });
    }

    @FXML
    private void deleteFiringPost() {
        DialogUtils.showConfirmationDialog("Suppression d'un poste de tir", "Supprimer un poste de tir",
                "Etes-vous sur de vouloir supprimer le poste de tir \"" + firingPostListView.getSelectionModel().getSelectedItem().getName() + "\"",
                () -> firingPointService.deleteFiringPost(firingPointListView.getSelectionModel().getSelectedItem(), firingPostListView.getSelectionModel().getSelectedItem()));
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
