package com.fligneul.srm.ui;

import javafx.application.Application;

/**
 * Application launcher
 * This class MUST NOT extends {@link Application} to load the JavaFX components at startup
 */
public class ShootingRangeManagerLauncher {

    /**
     * Start the application from the launcher
     *
     * @param args
     *         application command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        ShootingRangeManagerMain.main(args);
    }
}
