package com.fligneul.srm.ui.node.licensee;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.service.licensee.LicenseeSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import io.reactivex.rxjava3.core.Observable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.ListViewMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(ApplicationExtension.class)
class LicenseeListViewTest {
    private LicenseeListView licenseeListView;

    private final LicenseeServiceToJfxModel licenseeServiceToJfxModelMock = Mockito.mock(LicenseeServiceToJfxModel.class);
    private final LicenseeSelectionService licenseeSelectionServiceMock = Mockito.mock(LicenseeSelectionService.class);
    private final ObservableList<LicenseeJfxModel> licenseeJfxModels = FXCollections.observableArrayList();

    @Start
    private void start(Stage stage) {
        Mockito.when(licenseeServiceToJfxModelMock.getLicenseeList()).thenReturn(licenseeJfxModels);
        Mockito.when(licenseeSelectionServiceMock.selectedObs()).thenReturn(Observable.just(Optional.empty()));

        licenseeListView = new LicenseeListView();
        licenseeListView.injectDependencies(licenseeServiceToJfxModelMock, licenseeSelectionServiceMock);
        stage.setScene(new Scene(new StackPane(licenseeListView), 600, 400));
        stage.show();
    }

    @Test
    void licenseeListViewTest() {
        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasItems(0));

        LicenseeJfxModel licensee1 = (new LicenseeJfxModelBuilder())
                .setId(0)
                .setLicenceNumber("123")
                .setFirstName("FIRSTNAME_1")
                .setLastName("LASTNAME_1")
                .setDateOfBirth(LocalDate.EPOCH)
                .createLicenseeJfxModel();
        LicenseeJfxModel licensee2 = (new LicenseeJfxModelBuilder())
                .setId(1)
                .setLicenceNumber("456")
                .setFirstName("FIRSTNAME_2")
                .setLastName("LASTNAME_2")
                .setDateOfBirth(LocalDate.EPOCH)
                .createLicenseeJfxModel();
        LicenseeJfxModel licensee3 = (new LicenseeJfxModelBuilder())
                .setId(2)
                .setLicenceNumber("789")
                .setFirstName("AZERTY")
                .setLastName("QSDFGH")
                .setDateOfBirth(LocalDate.EPOCH)
                .createLicenseeJfxModel();

        licenseeJfxModels.addAll(licensee1, licensee2, licensee3);
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasItems(3));
        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasListCell(licensee1));
        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasListCell(licensee2));
        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasListCell(licensee3));

        // Test filter
        Platform.runLater(() -> licenseeListView.setFilter("FIRSTNAME"));
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasItems(2));
        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasListCell(licensee1));
        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasListCell(licensee2));

        Platform.runLater(() -> licenseeListView.setFilter("AZER"));
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasItems(1));
        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasListCell(licensee3));

        Platform.runLater(() -> licenseeListView.setFilter(null));
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasItems(3));
        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasListCell(licensee1));
        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasListCell(licensee2));
        FxAssert.verifyThat(licenseeListView, ListViewMatchers.hasListCell(licensee3));

        // Test selection
        licenseeListView.getSelectionModel().clearAndSelect(0);
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(licenseeSelectionServiceMock, Mockito.timeout(2_000)).setSelected(licensee1);
    }
}
