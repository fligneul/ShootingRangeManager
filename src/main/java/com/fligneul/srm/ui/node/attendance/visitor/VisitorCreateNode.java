package com.fligneul.srm.ui.node.attendance.visitor;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.component.ValidatedDatePicker;
import com.fligneul.srm.ui.component.ValidatedTextField;
import com.fligneul.srm.ui.component.ValidationUtils;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.time.LocalDate;

public class VisitorCreateNode extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(VisitorCreateNode.class);
    private static final String FXML_PATH = "visitorCreate.fxml";

    @FXML
    protected ValidatedTextField<String> firstnameTextField;
    @FXML
    protected ValidatedTextField<String> lastnameTextField;
    @FXML
    protected ValidatedDatePicker dateOfBirthPicker;
    @FXML
    protected ValidatedTextField<String> emailTextField;
    @FXML
    protected ValidatedTextField<String> phoneNumberTextField;

    private LicenseeServiceToJfxModel licenseeServiceToJfxModel;

    private LicenseeJfxModel currentLicenseeJfxModel;

    public VisitorCreateNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        firstnameTextField.setValidator(ValidationUtils.validateRequiredString());
        lastnameTextField.setValidator(ValidationUtils.validateRequiredString());
        emailTextField.setValidator(ValidationUtils.validateRequiredEmail());
        phoneNumberTextField.setValidator(ValidationUtils.validateRequiredPhoneNumber());
    }

    @Inject
    public void injectDependencies(final LicenseeServiceToJfxModel licenseeServiceToJfxModel) {
        this.licenseeServiceToJfxModel = licenseeServiceToJfxModel;
    }

    private void clearComponents() {
        currentLicenseeJfxModel = null;
    }

    @FXML
    private void saveLicensee() {
        final LicenseeJfxModelBuilder builder = new LicenseeJfxModelBuilder();

        licenseeServiceToJfxModel.saveLicensee(builder.createLicenseeJfxModel());
        clearComponents();
        closeStage();
    }

    @FXML
    private void cancel() {
        clearComponents();
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }

    public void prefillComponents(String firstname, String lastname, LocalDate birthDate) {
        firstnameTextField.setText(firstname);
        lastnameTextField.setText(lastname);
        dateOfBirthPicker.setValue(birthDate);
    }
}
