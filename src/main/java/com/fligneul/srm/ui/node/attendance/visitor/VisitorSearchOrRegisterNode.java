package com.fligneul.srm.ui.node.attendance.visitor;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.node.attendance.AttendanceLicenseeSelectorNode;
import com.fligneul.srm.ui.node.utils.ListViewUtils;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class VisitorSearchOrRegisterNode extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger(AttendanceLicenseeSelectorNode.class);
    private static final String FXML_PATH = "visitorSearchOrRegister.fxml";
    private static final int MAX_LEVENSHTEIN_DISTANCE = 3;
    private static final int MAX_LICENSEE_RESULT = 5;

    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private DatePicker birthDate;
    @FXML
    private VBox candidateContainer;
    @FXML
    private Label noCandidateLabel;
    @FXML
    private ListView<LicenseeJfxModel> candidateListView;
    @FXML
    private Button validateCandidateButton;
    @FXML
    private VisitorCreateNode visitorCreateNode;
    @FXML
    private Button createVisitorButton;
    @FXML
    private VBox visitorCreationContainer;

    private AttendanceSelectionService attendanceSelectionService;
    private LicenseeServiceToJfxModel licenseeServiceToJfxModel;

    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance(MAX_LEVENSHTEIN_DISTANCE);

    /**
     * Create the node and load the associated FXML file
     */
    public VisitorSearchOrRegisterNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        noCandidateLabel.visibleProperty().bind(candidateListView.visibleProperty().not());
        noCandidateLabel.managedProperty().bind(noCandidateLabel.visibleProperty());

        candidateListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<LicenseeJfxModel> call(ListView<LicenseeJfxModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(LicenseeJfxModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("");
                        } else {
                            setText(item.getFirstName() + " " + item.getLastName());
                        }
                    }
                };
            }
        });
        ListViewUtils.addClearOnEmptySelection(candidateListView);

        validateCandidateButton.disableProperty().bind(candidateListView.getSelectionModel().selectedItemProperty().isNull());
    }

    /**
     * Inject GUICE dependencies
     *
     * @param attendanceSelectionService
     *         selection service for the current licensee
     */
    @Inject
    public void injectDependencies(final AttendanceSelectionService attendanceSelectionService,
                                   final LicenseeServiceToJfxModel licenseeServiceToJfxModel) {
        this.attendanceSelectionService = attendanceSelectionService;
        this.licenseeServiceToJfxModel = licenseeServiceToJfxModel;
    }

    @FXML
    private void searchLicensee() {
        List<LicenseeJfxModel> a = licenseeServiceToJfxModel.getLicenseeList()
                .stream()
                .filter(licensee -> Optional.ofNullable(licensee.getDateOfBirth()).map(date -> date.equals(birthDate.getValue())).orElse(false))
                .filter(this::matchingLicensee)
                .sorted(Comparator.comparingInt((LicenseeJfxModel value) -> getLevenshteinDistance(value.getFirstName(), firstname.getText() + getLevenshteinDistance(value.getLastName(), lastname.getText()))).reversed())
                .limit(MAX_LICENSEE_RESULT)
                .collect(Collectors.toList());


        candidateContainer.setVisible(true);
        if (!a.isEmpty()) {
            candidateListView.setItems(FXCollections.observableList(a));
            candidateListView.setVisible(true);
            candidateListView.setManaged(true);
        } else {
            candidateListView.setItems(FXCollections.emptyObservableList());
            candidateListView.setVisible(false);
            candidateListView.setManaged(false);
        }
    }

    @FXML
    private void saveLicensee() {
        extracted();
    }

    @FXML
    private void cancel() {
        extracted();
    }

    private void extracted() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }

    @FXML
    private void validateCandidate() {
        extracted();
    }

    @FXML
    private void createVisitor() {
        visitorCreationContainer.setVisible(true);
        visitorCreationContainer.setManaged(true);

        visitorCreateNode.prefillComponents(firstname.getText(), lastname.getText(), birthDate.getValue());
    }

    private Integer getLevenshteinDistance(String left, String right) {
        return levenshteinDistance.apply(left.toUpperCase(Locale.getDefault()), right.toUpperCase(Locale.getDefault()));
    }

    private boolean matchingLicensee(LicenseeJfxModel licensee) {
        Integer firstnameDistance = getLevenshteinDistance(licensee.getFirstName(), firstname.getText());
        Integer lastNameDistance = getLevenshteinDistance(licensee.getLastName(), lastname.getText());
        return firstnameDistance >= 0 && firstnameDistance <= MAX_LEVENSHTEIN_DISTANCE && lastNameDistance >= 0 && lastNameDistance <= MAX_LEVENSHTEIN_DISTANCE;
    }
}
