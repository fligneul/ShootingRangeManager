package com.fligneul.srm.di;

import com.google.inject.Injector;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

/**
 * FXML loader with Guice injection
 */
public class FXMLGuiceNodeLoader {
    private static final Logger LOGGER = LogManager.getLogger(FXMLGuiceNodeLoader.class);

    private static Injector injector;

    private FXMLGuiceNodeLoader() {
    }

    /**
     * Save the injector in the loader for future FXML loading
     *
     * @param injector
     *         Guice injector
     */
    public static void setInjector(final Injector injector) {
        FXMLGuiceNodeLoader.injector = injector;
    }

    /**
     * Load a FXML in a shared root and controller node and inject Guice dependencies
     *
     * @param fxmlPath
     *         path of the FXML
     * @param sharedRootController
     *         Root and controller node for the provided FXML
     */
    public static void loadFxml(final String fxmlPath, final Node sharedRootController) {
        loadFxml(fxmlPath, sharedRootController, sharedRootController);
    }

    /**
     * Load a FXML in a root and a controller node and inject Guice dependencies
     *
     * @param fxmlPath
     *         path of the FXML
     * @param root
     *         Root node for the provided FXML
     * @param controller
     *         Controller for the loaded node
     */
    public static void loadFxml(final String fxmlPath, final Node root, final Node controller) {
        LOGGER.trace("Try to load {} FXML", fxmlPath);

        final FXMLLoader loader = new FXMLLoader(root.getClass().getResource(fxmlPath));
        loader.setRoot(root);
        loader.setController(controller);

        try {
            loader.load();
            LOGGER.trace("FXML {} loaded", fxmlPath);

            // If an injector is registered, load it on the provided node
            Optional.ofNullable(injector).ifPresent(injector -> {
                if (root.equals(controller)) {
                    injector.injectMembers(root);
                } else {
                    injector.injectMembers(root);
                    injector.injectMembers(controller);
                }
            });
        } catch (final IOException e) {
            LOGGER.error("Error while loading FXML : ", e);
            Platform.exit();
            System.exit(1);
        }

    }
}
