package com.fligneul.srm.ui;

import com.fligneul.srm.ui.node.main.MainNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main application
 * Build the {@link MainNode} and display it
 */
public class Main extends Application {
    private static final String APPLICATION_TITLE = "Shooting Range Manager";
    private static final String APPLICATION_ICON_PNG = "/ShootingRangeManager.png";
    private static final int APPLICATION_WIDTH = 1280;
    private static final int APPLICATION_HEIGHT = 720;

    /**
     * Launch the application
     *
     * @param args
     *         application command line arguments
     */
    public static void main(String[] args) {
        launch(Main.class);
    }

    /**
     * Start the application
     *
     * @param primaryStage
     *         the stage used to display the GUI
     */
    @Override
    public void start(Stage primaryStage) {
        // Create main node and display it
        MainNode mainNode = new MainNode();
        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream(APPLICATION_ICON_PNG))));
        primaryStage.setScene(new Scene(mainNode, APPLICATION_WIDTH, APPLICATION_HEIGHT));
        primaryStage.show();
    }
}
