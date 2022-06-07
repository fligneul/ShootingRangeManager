package com.fligneul.srm.ui.node.attendance.visitor;

import com.fligneul.srm.ui.node.PseudoClassMatchers;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextInputControlMatchers;

@ExtendWith(ApplicationExtension.class)
class VisitorCreateNodeTest {

   /* private VisitorCreateNode visitorCreateNode;

    @Start
    private void start(Stage stage) {
        visitorCreateNode = new VisitorCreateNode();
        visitorCreateNode.injectDependencies();
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
        FxAssert.verifyThat(visitorCreateNode.emailTextField, PseudoClassMatchers.withPseudoClass("error"));
        FxAssert.verifyThat(visitorCreateNode.phoneNumberTextField, PseudoClassMatchers.withPseudoClass("error"));

        fxRobot.clickOn(visitorCreateNode.firstnameTextField).write("TEST_FIRSTNAME");
        FxAssert.verifyThat(visitorCreateNode.firstnameTextField, PseudoClassMatchers.withoutPseudoClass("error"));

        fxRobot.clickOn(visitorCreateNode.lastnameTextField).write("TEST_LASTNAME");
        FxAssert.verifyThat(visitorCreateNode.lastnameTextField, PseudoClassMatchers.withoutPseudoClass("error"));

        fxRobot.clickOn(visitorCreateNode.dateOfBirthPicker).write("01/01/2000");
        FxAssert.verifyThat(visitorCreateNode.dateOfBirthPicker, PseudoClassMatchers.withoutPseudoClass("error"));

        fxRobot.clickOn(visitorCreateNode.emailTextField).write("test@test.com");
        FxAssert.verifyThat(visitorCreateNode.emailTextField, PseudoClassMatchers.withoutPseudoClass("error"));

        fxRobot.clickOn(visitorCreateNode.phoneNumberTextField).write("0123456789");
        FxAssert.verifyThat(visitorCreateNode.phoneNumberTextField, PseudoClassMatchers.withoutPseudoClass("error"));
    }*/
}

