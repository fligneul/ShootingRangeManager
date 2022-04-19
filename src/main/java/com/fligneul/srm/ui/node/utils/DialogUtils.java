package com.fligneul.srm.ui.node.utils;

import com.fligneul.srm.ui.ShootingRangeManagerMain;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class DialogUtils {
    private static final Logger LOGGER = LogManager.getLogger(DialogUtils.class);

    public static void showConfirmationDialog(final String title, final String text, final String contentText, final Runnable action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(contentText);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                action.run();
            } else if (buttonType == ButtonType.CANCEL) {
                LOGGER.info("Action canceled");
            } else {
                throw new IllegalArgumentException();
            }
        });
    }

    public static void showInformationDialog(final String title, final String text, final String contentText, final Runnable action) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(contentText);

        alert.showAndWait().ifPresent(any -> action.run());
    }

    public static void showErrorDialog(final String title, final String text, final String contentText, final Runnable action) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(contentText);

        alert.showAndWait().ifPresent(any -> action.run());
    }

    public static void showInputTextDialog(final String title, final String text, final String inputText, final String promptText, final Consumer<String> action) {
        TextInputDialog dialog = new TextInputDialog(promptText);

        dialog.setTitle(title);
        dialog.setHeaderText(text);
        dialog.setContentText(inputText);
        dialog.showAndWait().ifPresent(action);
    }

    public static <T extends Pane> void showCustomDialog(final String title, final T dialogPane) {
        Scene scene = new Scene(dialogPane);
        scene.getStylesheets().add(ShootingRangeManagerMain.class.getResource("/style.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.showAndWait();
    }

}
