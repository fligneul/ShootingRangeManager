package com.fligneul.srm.ui.node.settings.dialog;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModelBuilder;
import com.fligneul.srm.ui.service.status.StatusServiceToJfxModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Optional;

public class StatusDialog extends VBox {
    private static final Logger LOGGER = LogManager.getLogger(StatusDialog.class);
    private static final String FXML_PATH = "statusDialog.fxml";

    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField nameTextField;

    private StatusServiceToJfxModel statusServiceToJfxModel;
    private StatusJfxModel currentStatusJfxModel;

    public StatusDialog(final StatusJfxModel statusJfxModel) {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        Optional.ofNullable(statusJfxModel).ifPresent(this::updateComponents);
    }

    public StatusDialog() {
        this(null);
    }

    @Inject
    public void injectDependencies(final StatusServiceToJfxModel statusServiceToJfxModel) {
        this.statusServiceToJfxModel = statusServiceToJfxModel;
    }

    private void clearComponents() {
        currentStatusJfxModel = null;

        nameTextField.setText("");
    }

    private void updateComponents(StatusJfxModel statusJfxModel) {
        currentStatusJfxModel = statusJfxModel;

        nameTextField.setText(statusJfxModel.getName());
    }

    @FXML
    private void saveStatus() {
        final StatusJfxModelBuilder builder = new StatusJfxModelBuilder();

        builder.setId((currentStatusJfxModel != null ? currentStatusJfxModel.getId() : -1))
                .setName(nameTextField.getText());

        statusServiceToJfxModel.saveStatus(builder.createStatusJfxModel());
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
}
