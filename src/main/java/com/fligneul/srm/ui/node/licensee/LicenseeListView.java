package com.fligneul.srm.ui.node.licensee;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.node.utils.StringUtils;
import com.fligneul.srm.ui.service.licensee.LicenseeSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * List view for displaying licensee.
 * Display is composed of the licence number, the firstname and the lastname
 */
public class LicenseeListView extends ListView<LicenseeJfxModel> {
    private static final String FXML_PATH = "licenseeListView.fxml";
    private FilteredList<LicenseeJfxModel> filteredLicenseeJfxModels;

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
        filteredLicenseeJfxModels = new FilteredList<>(licenseeServiceToJfxModel.getLicenseeList());

        SortedList<LicenseeJfxModel> sortedLicenseeJfxModels = new SortedList<>(filteredLicenseeJfxModels);
        sortedLicenseeJfxModels.setComparator(Comparator.comparing(licenseeJfxModel -> Optional.ofNullable(licenseeJfxModel.getLastName()).map(String::toUpperCase).orElse(EMPTY)));
        setItems(sortedLicenseeJfxModels);

        getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> licenseeSelectionService.setSelected(newV));
        licenseeSelectionService.selectedObs().filter(Optional::isEmpty).subscribe(any -> getSelectionModel().clearSelection());
    }

    /**
     * Set the list filter. Must be run on Javafx Thread
     *
     * @param filter
     *         list filter to apply
     */
    public void setFilter(final String filter) {
        filteredLicenseeJfxModels.setPredicate(licenseeJfxModel ->
                Optional.ofNullable(filter)
                        .filter(Predicate.not(String::isEmpty))
                        .map(f -> StringUtils.containsIgnoreCase(licenseeJfxModel.getFirstName(), f) || StringUtils.containsIgnoreCase(licenseeJfxModel.getLastName(), f))
                        .orElse(Boolean.TRUE));
    }
}
