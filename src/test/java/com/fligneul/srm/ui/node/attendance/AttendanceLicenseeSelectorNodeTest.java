package com.fligneul.srm.ui.node.attendance;

import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class AttendanceLicenseeSelectorNodeTest {
    private AttendanceLicenseeSelectorNode attendanceLicenseeSelectorNode;
    private final AttendanceSelectionService attendanceSelectionServiceMock = Mockito.mock(AttendanceSelectionService.class);

    @Start
    private void start(Stage stage) {
        attendanceLicenseeSelectorNode = new AttendanceLicenseeSelectorNode();
        attendanceLicenseeSelectorNode.injectDependencies(attendanceSelectionServiceMock);
        stage.setScene(new Scene(new StackPane(attendanceLicenseeSelectorNode), 600, 400));
        stage.show();
    }

    @Test
    void attendanceLicenseeSelectorNodeTest(final FxRobot fxRobot) {
        Mockito.when(attendanceSelectionServiceMock.searchAndSelect("123")).thenReturn(false);
        Mockito.when(attendanceSelectionServiceMock.searchAndSelect("456")).thenReturn(true);

        FxAssert.verifyThat(attendanceLicenseeSelectorNode.licenceNumber, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSelectorNode.errorLabel, LabeledMatchers.hasText(""));
        FxAssert.verifyThat(attendanceLicenseeSelectorNode.errorLabel, NodeMatchers.isInvisible());

        // Licensee not found
        fxRobot.clickOn(attendanceLicenseeSelectorNode.licenceNumber).write("123");
        fxRobot.clickOn(attendanceLicenseeSelectorNode.validateButton);
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(attendanceLicenseeSelectorNode.errorLabel, LabeledMatchers.hasText("Aucun licencié enregistré avec ce numéro de licence"));
        FxAssert.verifyThat(attendanceLicenseeSelectorNode.errorLabel, NodeMatchers.isVisible());

        // Licensee found
        fxRobot.doubleClickOn(attendanceLicenseeSelectorNode.licenceNumber).write("456");
        fxRobot.clickOn(attendanceLicenseeSelectorNode.validateButton);
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(attendanceLicenseeSelectorNode.errorLabel, NodeMatchers.isInvisible());

        // Input error
        fxRobot.doubleClickOn(attendanceLicenseeSelectorNode.licenceNumber).write("abc");
        fxRobot.clickOn(attendanceLicenseeSelectorNode.validateButton);
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat(attendanceLicenseeSelectorNode.errorLabel, LabeledMatchers.hasText("Numéro de licence invalide"));
        FxAssert.verifyThat(attendanceLicenseeSelectorNode.errorLabel, NodeMatchers.isVisible());
    }
}
