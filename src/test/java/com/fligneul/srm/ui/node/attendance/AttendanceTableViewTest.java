package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModelBuilder;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModelBuilder;
import com.fligneul.srm.ui.service.attendance.AttendanceServiceToJfxModel;
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
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ExtendWith(ApplicationExtension.class)
class AttendanceTableViewTest {

    private AttendanceTableView attendanceTableView;
    private final AttendanceServiceToJfxModel attendanceServiceMock = Mockito.mock(AttendanceServiceToJfxModel.class);
    private final ObservableList<LicenseePresenceJfxModel> licenseePresenceJfxModels = FXCollections.observableArrayList();

    @Start
    private void start(Stage stage) {
        Mockito.when(attendanceServiceMock.getLicenseePresenceList()).thenReturn(licenseePresenceJfxModels);

        attendanceTableView = new AttendanceTableView();
        attendanceTableView.injectDependencies(attendanceServiceMock);
        stage.setScene(new Scene(new StackPane(attendanceTableView), 600, 400));
        stage.show();
    }

    @Test
    void attendanceTableViewTest() {
        FxAssert.verifyThat(attendanceTableView, TableViewMatchers.hasNumRows(0));

        licenseePresenceJfxModels.addAll(
                (new LicenseePresenceJfxModelBuilder())
                        .setLicensee((new LicenseeJfxModelBuilder())
                                .setLicenceNumber("123")
                                .setFirstName("FIRSTNAME_1")
                                .setLastName("LASTNAME_1")
                                .setDateOfBirth(LocalDate.EPOCH)
                                .createLicenseeJfxModel())
                        .setFiringPoint(new FiringPointJfxModel("TEST_1"))
                        .setStartDate(LocalDateTime.of(2022, 4, 30, 12, 30, 15))
                        .setStopDate(LocalDateTime.of(2022, 4, 30, 14, 30, 15))
                        .createLicenseePresenceJfxModel(),
                (new LicenseePresenceJfxModelBuilder())
                        .setLicensee((new LicenseeJfxModelBuilder())
                                .setLicenceNumber("456")
                                .setFirstName("FIRSTNAME_2")
                                .setLastName("LASTNAME_2")
                                .setDateOfBirth(LocalDate.EPOCH)
                                .createLicenseeJfxModel())
                        .setFiringPoint(new FiringPointJfxModel("TEST_2"))
                        .setFiringPost(new FiringPostJfxModel("TEST_2_1"))
                        .setWeapon((new WeaponJfxModelBuilder()).setName("TEST_WEAPON").setIdentificationNumber(42).createWeaponJfxModel())
                        .setStartDate(LocalDateTime.of(2022, 4, 30, 15, 30, 15))
                        .createLicenseePresenceJfxModel()
        );

        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(attendanceTableView, TableViewMatchers.hasNumRows(2));
        FxAssert.verifyThat(attendanceTableView, TableViewMatchers.containsRowAtIndex(0, "FIRSTNAME_1 LASTNAME_1", "-", "12:30", "TEST_1", "-", "-", "-", "-", "14:30"));
        FxAssert.verifyThat(attendanceTableView, TableViewMatchers.containsRowAtIndex(1, "FIRSTNAME_2 LASTNAME_2", "-", "15:30", "TEST_2", "TEST_2_1", "TEST_WEAPON", "-"));
    }
}
