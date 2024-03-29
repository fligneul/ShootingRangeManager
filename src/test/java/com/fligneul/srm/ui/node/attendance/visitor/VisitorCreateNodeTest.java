package com.fligneul.srm.ui.node.attendance.visitor;

import com.fligneul.srm.ui.node.PseudoClassMatchers;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class VisitorCreateNodeTest {
    private VisitorCreateNode visitorCreateNode;

    private final LicenseeServiceToJfxModel licenseeServiceToJfxModelMock = Mockito.mock(LicenseeServiceToJfxModel.class);

    @Start
    private void start(Stage stage) {
        Mockito.when(licenseeServiceToJfxModelMock.saveLicensee(ArgumentMatchers.any())).thenReturn(true);
        visitorCreateNode = new VisitorCreateNode();
        visitorCreateNode.injectDependencies(licenseeServiceToJfxModelMock);
        Scene scene = new Scene(new StackPane(visitorCreateNode), 500, 200);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void visitorCreateNodeTest(final FxRobot fxRobot) {
        FxAssert.verifyThat(visitorCreateNode.firstnameTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(visitorCreateNode.lastnameTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(visitorCreateNode.dateOfBirthPicker.getEditor(), TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(visitorCreateNode.emailTextField, TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat(visitorCreateNode.phoneNumberTextField, TextInputControlMatchers.hasText(""));

        FxAssert.verifyThat(visitorCreateNode.firstnameTextField, PseudoClassMatchers.withPseudoClass("error"));
        FxAssert.verifyThat(visitorCreateNode.lastnameTextField, PseudoClassMatchers.withPseudoClass("error"));
        FxAssert.verifyThat(visitorCreateNode.dateOfBirthPicker, PseudoClassMatchers.withPseudoClass("error"));
        FxAssert.verifyThat(visitorCreateNode.emailTextField, PseudoClassMatchers.withoutPseudoClass("error"));
        FxAssert.verifyThat(visitorCreateNode.phoneNumberTextField, PseudoClassMatchers.withoutPseudoClass("error"));

        fxRobot.clickOn(visitorCreateNode.firstnameTextField).write("TEST_FIRSTNAME");
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorCreateNode.firstnameTextField, PseudoClassMatchers.withoutPseudoClass("error"));

        fxRobot.clickOn(visitorCreateNode.lastnameTextField).write("TEST_LASTNAME");
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorCreateNode.lastnameTextField, PseudoClassMatchers.withoutPseudoClass("error"));

        fxRobot.clickOn(visitorCreateNode.dateOfBirthPicker.getEditor());
        visitorCreateNode.dateOfBirthPicker.getEditor().setText("01/01/2000");
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorCreateNode.dateOfBirthPicker, PseudoClassMatchers.withoutPseudoClass("error"));

        fxRobot.clickOn(visitorCreateNode.emailTextField).write("test@test.fr");
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorCreateNode.emailTextField, PseudoClassMatchers.withoutPseudoClass("error"));

        fxRobot.clickOn(visitorCreateNode.phoneNumberTextField).write("0123456789");
        WaitForAsyncUtils.waitForFxEvents();
        FxAssert.verifyThat(visitorCreateNode.phoneNumberTextField, PseudoClassMatchers.withoutPseudoClass("error"));
    }
}

