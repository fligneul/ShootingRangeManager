package com.fligneul.srm.ui.node.licensee;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.node.utils.DialogUtils;
import com.fligneul.srm.ui.node.utils.ListViewUtils;
import com.fligneul.srm.ui.service.licensee.LicenseeSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import javax.inject.Inject;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Licensee view node
 */
public class LicenseeNode extends StackPane {
    private static final String FXML_PATH = "licensee.fxml";

    @FXML
    private TextField licenseeFilterTextField;
    @FXML
    private LicenseeListView licenseeListView;
    @FXML
    private LicenseeDetailNode licenseeDetailNode;

    private LicenseeSelectionService licenseeSelectionService;
    private LicenseeServiceToJfxModel licenseeServiceToJfxModel;

    public LicenseeNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
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
    public void injectDependencies(final LicenseeSelectionService licenseeSelectionService,
                                   final LicenseeServiceToJfxModel licenseeServiceToJfxModel) {
        this.licenseeSelectionService = licenseeSelectionService;
        this.licenseeServiceToJfxModel = licenseeServiceToJfxModel;

        ListViewUtils.addClearOnEmptySelection(licenseeListView);

        licenseeFilterTextField.textProperty().addListener((obs, oldV, newV) -> licenseeListView.setFilter(newV));
    }

    @FXML
    private void createLicensee() {
        licenseeSelectionService.setSelected(null);
        DialogUtils.showCustomDialog("Création d'un licencié", new LicenseeCreateNode());
    }

    @FXML
    private void importLicensee() {
        licenseeSelectionService.setSelected(null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importation des licenciés");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            try {
                Reader in = null;
                in = new FileReader(file, StandardCharsets.ISO_8859_1);
                Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().withDelimiter(';').parse(in);

                for (CSVRecord record : records) {
                    final LicenseeJfxModelBuilder builder = new LicenseeJfxModelBuilder();

                    builder.setId(LicenseeJfxModel.DEFAULT_ID)
                            .setFirstName(record.get("Prénom"))
                            .setLastName(record.get("Nom"))
                            .setDateOfBirth(LocalDate.parse(record.get("Date de naissance"), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                            .setHandisport(record.get("Handisport").equalsIgnoreCase("OUI"))
                            .setBlacklisted(record.get("Etat (Blacklistage)").equalsIgnoreCase("OUI"));

                    Optional.ofNullable(record.get("N° Licence")).ifPresent(builder::setLicenceNumber);
                    Optional.ofNullable(record.get("Nom de jeune fille")).ifPresent(builder::setMaidenName);
                    Optional.ofNullable(record.get("Sexe")).ifPresent(builder::setSex);
                    Optional.ofNullable(record.get("Lieu de naissance")).ifPresent(builder::setPlaceOfBirth);
                    Optional.ofNullable(record.get("Département de naissance")).ifPresent(builder::setDepartmentOfBirth);
                    Optional.ofNullable(record.get("Pays de naissance")).ifPresent(builder::setCountryOfBirth);
                    Optional.ofNullable(record.get("Adresse 1")).ifPresent(builder::setAddress);
                    Optional.ofNullable(record.get("Code Postal")).ifPresent(builder::setZipCode);
                    Optional.ofNullable(record.get("Ville")).ifPresent(builder::setCity);
                    Optional.ofNullable(record.get("Email")).ifPresent(builder::setEmail);
                    Optional.ofNullable(record.get("Téléphone portable")).ifPresent(builder::setPhoneNumber);
                    Optional.ofNullable(record.get("Etat de la licence")).ifPresent(builder::setLicenceState);
                    Optional.ofNullable(record.get("Date d'origine")).map(date -> LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))).ifPresent(builder::setFirstLicenceDate);
                    Optional.ofNullable(record.get("Saison")).ifPresent(builder::setSeason);
                    Optional.ofNullable(record.get("Code catégorie d'âge")).ifPresent(builder::setAgeCategory);

                    licenseeServiceToJfxModel.saveLicensee(builder.createLicenseeJfxModel());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void clearFilter() {
        licenseeFilterTextField.clear();
    }
}
