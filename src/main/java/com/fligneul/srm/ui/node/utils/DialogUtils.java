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

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.NOTHING_TO_DO;

/**
 * Utility class for JavaFX dialogs
 */
public class DialogUtils {
    private static final Logger LOGGER = LogManager.getLogger(DialogUtils.class);

    /**
     * Show confirmation dialog with an "OK" and "Cancel" button
     *
     * @param title
     *         dialog title
     * @param text
     *         dialog header text
     * @param contentText
     *         dialog content text
     * @param action
     *         dialog action on "OK" button
     */
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

    /**
     * Show information dialog with an "OK" button
     *
     * @param title
     *         dialog title
     * @param text
     *         dialog header text
     * @param contentText
     *         dialog content text
     */
    public static void showInformationDialog(final String title, final String text, final String contentText) {
        showInformationDialog(title, text, contentText, NOTHING_TO_DO);
    }

    /**
     * Show information dialog with an "OK" button
     *
     * @param title
     *         dialog title
     * @param text
     *         dialog header text
     * @param contentText
     *         dialog content text
     * @param action
     *         dialog action on "OK" button
     */
    public static void showInformationDialog(final String title, final String text, final String contentText, final Runnable action) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(contentText);

        alert.showAndWait().ifPresent(any -> action.run());
    }

    /**
     * Show error dialog with an "OK" button
     *
     * @param title
     *         dialog title
     * @param text
     *         dialog header text
     * @param contentText
     *         dialog content text
     */
    public static void showErrorDialog(final String title, final String text, final String contentText) {
        showErrorDialog(title, text, contentText, NOTHING_TO_DO);
    }

    /**
     * Show error dialog with an "OK" button
     *
     * @param title
     *         dialog title
     * @param text
     *         dialog header text
     * @param contentText
     *         dialog content text
     * @param action
     *         dialog action on "OK" button
     */
    public static void showErrorDialog(final String title, final String text, final String contentText, final Runnable action) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(contentText);

        alert.showAndWait().ifPresent(any -> action.run());
    }

    /**
     * Show a dialog with an input text field
     *
     * @param title
     *         dialog title
     * @param text
     *         dialog header text
     * @param inputText
     *         dialog content text
     * @param promptText
     *         dialog textfield prompt text
     * @param action
     *         dialog action on "OK" button
     */
    public static void showInputTextDialog(final String title, final String text, final String inputText, final String promptText, final Consumer<String> action) {
        TextInputDialog dialog = new TextInputDialog(promptText);

        dialog.setTitle(title);
        dialog.setHeaderText(text);
        dialog.setContentText(inputText);
        dialog.showAndWait().ifPresent(action);
    }

    /**
     * Show a dialog with a custom node
     *
     * @param title
     *         dialog title
     * @param dialogPane
     *         dialog content node
     */
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
