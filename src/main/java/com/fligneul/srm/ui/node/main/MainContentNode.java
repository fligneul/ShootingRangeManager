package com.fligneul.srm.ui.node.main;

import com.fligneul.srm.di.FXMLGuiceNodeLoader;
import com.fligneul.srm.service.AuthenticationService;
import com.fligneul.srm.service.RoleService;
import com.fligneul.srm.ui.model.user.ERole;
import com.fligneul.srm.ui.node.attendance.AttendanceNode;
import com.fligneul.srm.ui.node.history.HistoryNode;
import com.fligneul.srm.ui.node.licensee.LicenseeNode;
import com.fligneul.srm.ui.node.settings.SettingsNode;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Content node of the application
 * Display all views in a TabPane and a disconnection button
 */
public class MainContentNode extends AnchorPane {
    private static final Logger LOGGER = LogManager.getLogger(MainNode.class);
    private static final String FXML_PATH = "mainContent.fxml";

    private AuthenticationService authenticationService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @FXML
    private TabPane mainTabPane;

    public MainContentNode() {
        FXMLGuiceNodeLoader.loadFxml(FXML_PATH, this);

        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if (nv != null) {
                LOGGER.info("Display tab {}", nv.getId());
            }
        });
    }

    /**
     * Inject GUICE dependencies
     *
     * @param authenticationService
     *         user authentication service
     * @param roleService
     *         user role service
     */
    @Inject
    public void injectDependencies(final AuthenticationService authenticationService,
                                   final RoleService roleService) {
        this.authenticationService = authenticationService;

        compositeDisposable.add(roleService.getRoleObs().distinctUntilChanged()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(this::manageTabsForRole));
    }

    private void manageTabsForRole(ERole role) {
        mainTabPane.getTabs().clear();
        mainTabPane.getTabs().add(createTab("Émargement", new AttendanceNode()));
        if (ERole.ADMINISTRATOR.equals(role)) {
            mainTabPane.getTabs().add(createTab("Historique", new HistoryNode()));
            mainTabPane.getTabs().add(createTab("Licenciés", new LicenseeNode()));
            mainTabPane.getTabs().add(createTab("Réglages", new SettingsNode()));
        }
    }

    @FXML
    public void disconnect() {
        LOGGER.info("Disconnect current user");
        compositeDisposable.clear();
        authenticationService.disconnect();
    }

    private Tab createTab(String name, Node content) {
        Tab tab = new Tab(name);
        tab.setId(content.getClass().getSimpleName());
        tab.setContent(content);
        tab.setClosable(false);
        return tab;
    }
}
