package com.fligneul.srm.ui.node.main;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.AuthenticationService;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.node.firststart.FirstStartNode;
import com.fligneul.srm.ui.node.login.LoginNode;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Main node of the application
 * It is a container for the applications views
 */
public class MainNode extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger(MainNode.class);
    private static final String FXML_PATH = "main.fxml";

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);
    }

    /**
     * Inject Guice dependencies
     *
     * @param authenticationService
     *         {@link AuthenticationService}
     * @param databaseConnectionService
     *         {@link DatabaseConnectionService}
     */
    @Inject
    public void injectDependencies(final AuthenticationService authenticationService,
                                   final DatabaseConnectionService databaseConnectionService) {
        compositeDisposable.add(
                authenticationService.getAuthenticatedObs()
                        .distinctUntilChanged()
                        .observeOn(JavaFxScheduler.platform())
                        .subscribe(isAuth -> {
                            if (!databaseConnectionService.isDatabaseCreated()) {
                                LOGGER.info("Database not present, showing first start menu");
                                getChildren().setAll(new FirstStartNode());
                            } else {
                                LOGGER.info(isAuth ? "User successfully authenticated" : "No user authenticated");
                                getChildren().setAll(isAuth ? new MainContentNode() : new LoginNode());
                            }
                        }, e -> LOGGER.error("Authentication error", e)));
    }
}
