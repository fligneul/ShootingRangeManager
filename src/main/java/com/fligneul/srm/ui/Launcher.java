package com.fligneul.srm.ui;

import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Application launcher
 * This class MUST NOT extends {@link Application} to load the JavaFX components at startup
 */
public class Launcher {
    private static final Logger LOGGER = LogManager.getLogger(Launcher.class);

    /**
     * Start the application from the launcher
     *
     * @param args
     *         application command line arguments
     */
    public static void main(String[] args) {
        LOGGER.debug("Start application with args {}", (Object[]) args);
        Main.main(args);
    }
}
