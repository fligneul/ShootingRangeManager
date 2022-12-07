package com.fligneul.srm.ui.node.attendance.visitor;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.ListViewMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.time.LocalDate;

@ExtendWith(ApplicationExtension.class)
class VisitorSearchOrRegisterNodeTest {
    private VisitorSearchOrRegisterNode visitorSearchOrRegisterNode;

    private final AttendanceSelectionService attendanceSelectionServiceMock = Mockito.mock(AttendanceSelectionService.class);
    private final LicenseeServiceToJfxModel licenseeServiceToJfxModelMock = Mockito.mock(LicenseeServiceToJfxModel.class);

    @Start
    private void start(Stage stage) {
        visitorSearchOrRegisterNode = new VisitorSearchOrRegisterNode();
        visitorSearchOrRegisterNode.injectDependencies(attendanceSelectionServiceMock, licenseeServiceToJfxModelMock);
        Scene scene = new Scene(new StackPane(visitorSearchOrRegisterNode), 500, 300);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void searchLicenseeRegisteredTest(final FxRobot fxRobot) {
        FxAssert.verifyThat(visitorSearchOrRegisterNode.firstname, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(visitorSearchOrRegisterNode.lastname, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(visitorSearchOrRegisterNode.birthDate.getEditor(), TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(visitorSearchOrRegisterNode.searchButton, NodeMatchers.isDisabled());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.candidateContainer, NodeMatchers.isInvisible());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.candidateListView, NodeMatchers.isInvisible());

        fxRobot.clickOn(visitorSearchOrRegisterNode.firstname).write("TEST_FIRSTNAME");
        fxRobot.clickOn(visitorSearchOrRegisterNode.lastname).write("TEST_LASTNAME");
        fxRobot.clickOn(visitorSearchOrRegisterNode.birthDate.getEditor());
        visitorSearchOrRegisterNode.birthDate.getEditor().setText("01/01/2000");
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorSearchOrRegisterNode.searchButton, NodeMatchers.isEnabled());

        LicenseeJfxModel licenseeJfxModel = new LicenseeJfxModelBuilder()
                .setFirstName("TEST_FIRSTNAME")
                .setLastName("TEST_LASTNAME")
                .setDateOfBirth(LocalDate.of(2000, 1, 1))
                .createLicenseeJfxModel();
        Mockito.when(licenseeServiceToJfxModelMock.getLicenseeList()).thenReturn(FXCollections.observableArrayList(licenseeJfxModel));

        fxRobot.clickOn(visitorSearchOrRegisterNode.searchButton);
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorSearchOrRegisterNode.candidateContainer, NodeMatchers.isVisible());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.noCandidateLabel, NodeMatchers.isInvisible());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.candidateListView, NodeMatchers.isVisible());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.candidateListView, ListViewMatchers.hasItems(1));
        FxAssert.verifyThat(visitorSearchOrRegisterNode.validateCandidateButton, NodeMatchers.isDisabled());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.createVisitorButton, NodeMatchers.isEnabled());

        visitorSearchOrRegisterNode.candidateListView.getSelectionModel().selectFirst();
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorSearchOrRegisterNode.validateCandidateButton, NodeMatchers.isEnabled());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.createVisitorButton, NodeMatchers.isDisabled());

        fxRobot.clickOn(visitorSearchOrRegisterNode.validateCandidateButton);
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(attendanceSelectionServiceMock, Mockito.timeout(1_000)).select(licenseeJfxModel);
    }

    @Test
    void searchLicenseeNotRegisteredTest(final FxRobot fxRobot) {
        FxAssert.verifyThat(visitorSearchOrRegisterNode.firstname, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(visitorSearchOrRegisterNode.lastname, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(visitorSearchOrRegisterNode.birthDate.getEditor(), TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(visitorSearchOrRegisterNode.searchButton, NodeMatchers.isDisabled());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.candidateContainer, NodeMatchers.isInvisible());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.candidateListView, NodeMatchers.isInvisible());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.visitorCreationContainer, NodeMatchers.isInvisible());


        fxRobot.clickOn(visitorSearchOrRegisterNode.firstname).write("TEST_FIRSTNAME");
        fxRobot.clickOn(visitorSearchOrRegisterNode.lastname).write("TEST_LASTNAME");
        fxRobot.clickOn(visitorSearchOrRegisterNode.birthDate.getEditor());
        visitorSearchOrRegisterNode.birthDate.getEditor().setText("01/01/2000");
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorSearchOrRegisterNode.searchButton, NodeMatchers.isEnabled());

        Mockito.when(licenseeServiceToJfxModelMock.getLicenseeList()).thenReturn(FXCollections.emptyObservableList());
        fxRobot.clickOn(visitorSearchOrRegisterNode.searchButton);
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorSearchOrRegisterNode.candidateContainer, NodeMatchers.isVisible());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.noCandidateLabel, NodeMatchers.isVisible());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.candidateListView, NodeMatchers.isInvisible());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.validateCandidateButton, NodeMatchers.isDisabled());
        FxAssert.verifyThat(visitorSearchOrRegisterNode.createVisitorButton, NodeMatchers.isEnabled());

        fxRobot.clickOn(visitorSearchOrRegisterNode.createVisitorButton);
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorSearchOrRegisterNode.visitorCreationContainer, NodeMatchers.isVisible());

        Assertions.assertEquals(visitorSearchOrRegisterNode.visitorCreateNode.firstnameTextField.getText(), "TEST_FIRSTNAME");
        Assertions.assertEquals(visitorSearchOrRegisterNode.visitorCreateNode.lastnameTextField.getText(), "TEST_LASTNAME");
        Assertions.assertEquals(visitorSearchOrRegisterNode.visitorCreateNode.dateOfBirthPicker.getValue(), LocalDate.of(2000, 1, 1));

        LicenseeJfxModel licenseeJfxModel = new LicenseeJfxModelBuilder()
                .setFirstName("TEST_FIRSTNAME")
                .setLastName("TEST_LASTNAME")
                .setDateOfBirth(LocalDate.of(2000, 1, 1))
                .createLicenseeJfxModel();
        Mockito.when(licenseeServiceToJfxModelMock.getLicenseeList()).thenReturn(FXCollections.observableArrayList(licenseeJfxModel));
        fxRobot.clickOn(visitorSearchOrRegisterNode.saveButton);
        WaitForAsyncUtils.waitForFxEvents();
        Mockito.verify(licenseeServiceToJfxModelMock, Mockito.timeout(1_000)).saveLicensee(ArgumentMatchers.any());
        Mockito.verify(attendanceSelectionServiceMock, Mockito.timeout(1_000)).select(ArgumentMatchers.any());
    }
}

