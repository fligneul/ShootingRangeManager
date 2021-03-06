package com.fligneul.srm.ui.node.licensee;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.service.licensee.LicenseeSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * List view for displaying licensee.
 * Display is composed of the licence number, the firstname and the lastname
 */
public class LicenseeListView extends ListView<LicenseeJfxModel> {
    private static final String FXML_PATH = "licenseeListView.fxml";

    public LicenseeListView() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        setCellFactory(new Callback<>() {
            @Override
            public ListCell<LicenseeJfxModel> call(ListView<LicenseeJfxModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(LicenseeJfxModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(EMPTY);
                        } else {
                            setText(item.getLicenceNumber() + " " + item.getFirstName() + " " + item.getLastName());
                        }
                    }
                };
            }
        });
    }

    /**
     * Inject GUICE dependencies
     *
     * @param licenseeServiceToJfxModel
     *         service to jfx model for licensee
     * @param licenseeSelectionService
     *         service for the current selected licensee
     */
    @Inject
    public void injectDependencies(final LicenseeServiceToJfxModel licenseeServiceToJfxModel,
                                   final LicenseeSelectionService licenseeSelectionService) {
        SortedList<LicenseeJfxModel> licenseeJfxModels = new SortedList<>(licenseeServiceToJfxModel.getLicenseeList());
        licenseeJfxModels.setComparator(Comparator.comparing(licenseeJfxModel -> Optional.ofNullable(licenseeJfxModel.getLastName()).orElse(EMPTY)));
        setItems(licenseeJfxModels);

        getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> licenseeSelectionService.setSelected(newV));
        licenseeSelectionService.selectedObs().filter(Optional::isEmpty).subscribe(any -> getSelectionModel().clearSelection());
    }

}
