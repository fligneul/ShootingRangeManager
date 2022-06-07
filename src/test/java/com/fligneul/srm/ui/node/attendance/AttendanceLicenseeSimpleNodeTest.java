package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import com.fligneul.srm.ui.service.logbook.ShootingLogbookServiceToJfxModel;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(ApplicationExtension.class)
class AttendanceLicenseeSimpleNodeTest {

    private AttendanceLicenseeSimpleNode attendanceLicenseeSimpleNode;
    private final AttendanceSelectionService attendanceSelectionServiceMock = Mockito.mock(AttendanceSelectionService.class);
    private final ShootingLogbookServiceToJfxModel shootingLogbookServiceToJfxModelMock = Mockito.mock(ShootingLogbookServiceToJfxModel.class);
    private final BehaviorSubject<Optional<LicenseeJfxModel>> selectedObs = BehaviorSubject.createDefault(Optional.empty());

    @Start
    private void start(Stage stage) {
        Mockito.when(attendanceSelectionServiceMock.selectedObs()).thenReturn(selectedObs);
        Mockito.when(shootingLogbookServiceToJfxModelMock.getShootingLogbookList()).thenReturn(FXCollections.emptyObservableList());

        attendanceLicenseeSimpleNode = new AttendanceLicenseeSimpleNode();
        attendanceLicenseeSimpleNode.injectDependencies(attendanceSelectionServiceMock, shootingLogbookServiceToJfxModelMock);
        stage.setScene(new Scene(new StackPane(attendanceLicenseeSimpleNode), 600, 400));
        stage.show();
    }

    @Test
    void attendanceLicenseeSimpleNodeTest() {
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceNumberTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.firstnameTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.lastnameTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.maidenNameTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.dateOfBirthTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceStateTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.seasonTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.firstLicenceDateTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.ageCategoryTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.handisportCheckBox, checkBox -> !checkBox.isSelected());
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceErrorLabel, NodeMatchers.isInvisible());
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceBlacklistLabel, NodeMatchers.isInvisible());

        selectedObs.onNext(Optional.of((new LicenseeJfxModelBuilder())
                .setLicenceNumber("123")
                .setFirstName("FIRSTNAME_1")
                .setLastName("LASTNAME_1")
                .setDateOfBirth(LocalDate.EPOCH)
                .setMaidenName("MAIDEN_NAME_1")
                .setHandisport(true)
                .setLicenceState("LICENCE_STATE_1")
                .setFirstLicenceDate(LocalDate.of(1990, 1, 1))
                .setSeason("SEASON_1")
                .setAgeCategory("CATEGORY_1")
                .setBlacklisted(true)
                .createLicenseeJfxModel()));

        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceNumberTextField, TextInputControlMatchers.hasText("123"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.firstnameTextField, TextInputControlMatchers.hasText("FIRSTNAME_1"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.lastnameTextField, TextInputControlMatchers.hasText("LASTNAME_1"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.maidenNameTextField, TextInputControlMatchers.hasText("MAIDEN_NAME_1"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.dateOfBirthTextField, TextInputControlMatchers.hasText("01/01/1970"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceStateTextField, TextInputControlMatchers.hasText("LICENCE_STATE_1"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.seasonTextField, TextInputControlMatchers.hasText("SEASON_1"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.firstLicenceDateTextField, TextInputControlMatchers.hasText("01/01/1990"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.ageCategoryTextField, TextInputControlMatchers.hasText("CATEGORY_1"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.handisportCheckBox, CheckBox::isSelected);
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceErrorLabel, NodeMatchers.isVisible());
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceBlacklistLabel, NodeMatchers.isVisible());

        selectedObs.onNext(Optional.of((new LicenseeJfxModelBuilder())
                .setLicenceNumber("456")
                .setFirstName("FIRSTNAME_2")
                .setLastName("LASTNAME_2")
                .setDateOfBirth(LocalDate.EPOCH)
                .setHandisport(false)
                .setLicenceState("LICENCE_STATE_2")
                .setFirstLicenceDate(LocalDate.of(2000, 6, 1))
                .setBlacklisted(false)
                .setIdCardDate(LocalDate.of(2022, 1, 1))
                .setMedicalCertificateDate(LocalDate.of(2022, 1, 1))
                .setIdPhoto(true)
                .createLicenseeJfxModel()));

        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceNumberTextField, TextInputControlMatchers.hasText("456"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.firstnameTextField, TextInputControlMatchers.hasText("FIRSTNAME_2"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.lastnameTextField, TextInputControlMatchers.hasText("LASTNAME_2"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.dateOfBirthTextField, TextInputControlMatchers.hasText("01/01/1970"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceStateTextField, TextInputControlMatchers.hasText("LICENCE_STATE_2"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.firstLicenceDateTextField, TextInputControlMatchers.hasText("01/06/2000"));
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.handisportCheckBox, checkBox -> !checkBox.isSelected());
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceErrorLabel, NodeMatchers.isInvisible());
        FxAssert.verifyThat(attendanceLicenseeSimpleNode.licenceBlacklistLabel, NodeMatchers.isInvisible());
    }
}
