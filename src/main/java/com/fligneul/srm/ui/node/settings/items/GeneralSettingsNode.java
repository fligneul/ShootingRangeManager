package com.fligneul.srm.ui.node.settings.items;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.PreferenceService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import javax.inject.Inject;

/**
 * General configuration node
 */
public class GeneralSettingsNode extends StackPane implements ISettingsItemNode {
    private static final String FXML_PATH = "generalSettings.fxml";
    private static final String TITLE = "Général";

    @FXML
    private TextField shootingRangeNameTextField;
    @FXML
    private CheckBox medicalCertificateValidityInfiniteCheckBox;
    @FXML
    private Spinner<Integer> medicalCertificateValiditySpinner;
    @FXML
    private Spinner<Integer> medicalCertificateValidityAlertSpinner;
    private PreferenceService preferenceService;

    public GeneralSettingsNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

    }

    /**
     * Inject GUICE dependencies
     *
     * @param preferenceService
     *         application preference service
     */
    @Inject
    public void injectDependencies(final PreferenceService preferenceService) {
        this.preferenceService = preferenceService;

        shootingRangeNameTextField.setText(preferenceService.getShootingRangeName());

        medicalCertificateValidityInfiniteCheckBox.selectedProperty().addListener((obs, oldV, newV) -> {
            medicalCertificateValiditySpinner.setDisable(newV);
            medicalCertificateValidityAlertSpinner.setDisable(newV);
        });
        medicalCertificateValidityInfiniteCheckBox.setSelected(preferenceService.getMedicalCertificateValidityInfinite());

        medicalCertificateValiditySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, preferenceService.getMedicalCertificateValidityPeriod()));
        medicalCertificateValidityAlertSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, preferenceService.getMedicalCertificateValidityAlert()));
    }

    @FXML
    private void save() {
        preferenceService.saveShootingRangeName(shootingRangeNameTextField.getText());

        preferenceService.saveMedicalCertificateValidityInfinite(medicalCertificateValidityInfiniteCheckBox.isSelected());
        preferenceService.saveMedicalCertificateValidityPeriod(medicalCertificateValiditySpinner.getValue());
        preferenceService.saveMedicalCertificateValidityAlert(medicalCertificateValidityAlertSpinner.getValue());
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
