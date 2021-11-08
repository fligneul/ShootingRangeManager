package com.fligneul.srm.ui;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.di.module.DatabaseModule;
import com.fligneul.srm.di.module.ServiceModule;
import com.fligneul.srm.di.module.UIModule;
import com.fligneul.srm.ui.node.main.MainNode;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main application
 * Build the {@link MainNode} and display it
 */
public class ShootingRangeManagerMain extends Application {
    private static final String APPLICATION_TITLE = "Shooting Range Manager";
    private static final String APPLICATION_ICON_PNG = "/ShootingRangeManager.png";
    private static final int APPLICATION_WIDTH = 1200;
    private static final int APPLICATION_HEIGHT = 675;

    private static final AbstractModule[] MODULES = new AbstractModule[]{
            new DatabaseModule(),
            new ServiceModule(),
            new UIModule()
    };

    /**
     * Launch the application
     *
     * @param args
     *         application command line arguments
     */
    public static void main(String[] args) {
        launch(ShootingRangeManagerMain.class);
    }

    /**
     * Start the application
     *
     * @param primaryStage
     *         the stage used to display the GUI
     */
    @Override
    public void start(Stage primaryStage) {
        // Initialize FXML loader with Guice injector
        FXMLGuiceNodeLoader.setInjector(Guice.createInjector(MODULES));

        // Create main node and display it
        MainNode mainNode = new MainNode();
        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(ShootingRangeManagerMain.class.getResourceAsStream(APPLICATION_ICON_PNG))));
        primaryStage.setScene(new Scene(mainNode, APPLICATION_WIDTH, APPLICATION_HEIGHT));
        primaryStage.show();
    }
}
