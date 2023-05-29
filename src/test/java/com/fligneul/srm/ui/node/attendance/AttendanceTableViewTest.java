package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModel;
import com.fligneul.srm.ui.model.presence.LicenseePresenceJfxModelBuilder;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModelBuilder;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
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
import java.util.Optional;

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

        LicenseeJfxModel licensee1 = new LicenseeJfxModelBuilder()
                .setLicenceNumber("123")
                .setFirstName("FIRSTNAME_1")
                .setLastName("LASTNAME_1")
                .setDateOfBirth(LocalDate.EPOCH)
                .createLicenseeJfxModel();
        FiringPointJfxModel firingPoint1 = new FiringPointJfxModel("TEST_1");
        LocalDateTime startDate1 = LocalDateTime.of(2022, 4, 30, 12, 30, 15);
        LocalDateTime stopDate1 = LocalDateTime.of(2022, 4, 30, 14, 30, 15);
        LicenseePresenceJfxModel licenseePresence1 = new LicenseePresenceJfxModelBuilder()
                .setLicensee(licensee1)
                .setFiringPoint(firingPoint1)
                .setStartDate(startDate1)
                .setStopDate(stopDate1)
                .createLicenseePresenceJfxModel();

        LicenseeJfxModel licensee2 = new LicenseeJfxModelBuilder()
                .setLicenceNumber("456")
                .setFirstName("FIRSTNAME_2")
                .setLastName("LASTNAME_2")
                .setDateOfBirth(LocalDate.EPOCH)
                .createLicenseeJfxModel();
        FiringPointJfxModel firingPoint2 = new FiringPointJfxModel("TEST_2");
        FiringPostJfxModel firingPost2 = new FiringPostJfxModel("TEST_2_1");
        WeaponJfxModel weapon2 = new WeaponJfxModelBuilder().setName("TEST_WEAPON").setIdentificationNumber(42).createWeaponJfxModel();
        StatusJfxModel status2 = new StatusJfxModelBuilder().setName("TEST_STATUS").createStatusJfxModel();
        LocalDateTime startDate2 = LocalDateTime.of(2022, 4, 30, 12, 30, 15);
        LicenseePresenceJfxModel licenseePresence2 = new LicenseePresenceJfxModelBuilder()
                .setLicensee(licensee2)
                .setFiringPoint(firingPoint2)
                .setFiringPost(firingPost2)
                .setWeapon(weapon2)
                .setStatus(status2)
                .setStartDate(startDate2)
                .createLicenseePresenceJfxModel();

        licenseePresenceJfxModels.addAll(licenseePresence1, licenseePresence2);

        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(attendanceTableView, TableViewMatchers.hasNumRows(2));
        FxAssert.verifyThat(attendanceTableView, TableViewMatchers.containsRowAtIndex(0, licensee1, Optional.empty(), startDate1, firingPoint1, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(stopDate1)));
        FxAssert.verifyThat(attendanceTableView, TableViewMatchers.containsRowAtIndex(1, licensee2, Optional.of(status2), startDate2, firingPoint2, Optional.of(firingPost2), Optional.of(weapon2), Optional.empty()));
    }
}
