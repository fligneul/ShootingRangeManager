package com.fligneul.srm.ui.node;

import javafx.css.PseudoClass;
import javafx.css.Styleable;
import org.hamcrest.Matcher;
import org.testfx.matcher.base.GeneralMatchers;

public class PseudoClassMatchers {

    public static Matcher<Styleable> withPseudoClass(String pseudoClass) {
        String descriptionText = "with CSS pseudoClass \"" + pseudoClass + "\"";
        return GeneralMatchers.typeSafeMatcher(Styleable.class, descriptionText,
                (styleable) -> "\"" + styleable.getPseudoClassStates() + "\"",
                (styleable) -> styleable.getPseudoClassStates().contains(PseudoClass.getPseudoClass(pseudoClass)));
    }

    public static Matcher<Styleable> withoutPseudoClass(String pseudoClass) {
        String descriptionText = "without CSS pseudoClass \"" + pseudoClass + "\"";
        return GeneralMatchers.typeSafeMatcher(Styleable.class, descriptionText,
                (styleable) -> "\"" + styleable.getPseudoClassStates() + "\"",
                (styleable) -> !styleable.getPseudoClassStates().contains(PseudoClass.getPseudoClass(pseudoClass)));
    }
}
