package com.fligneul.srm.service;

import javafx.application.Platform;

/**
 * Handle application shutdown gracefully
 */
public class ShutdownService {

    /**
     * Shutdown application
     */
    public void shutdown() {
        Platform.exit();
        System.exit(0);
    }
}
