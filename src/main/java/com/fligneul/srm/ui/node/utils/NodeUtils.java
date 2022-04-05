package com.fligneul.srm.ui.node.utils;

import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;

public class NodeUtils {
    public static void changeFontIcon(final Button fontIconButton, final String iconLiteral, final Paint iconColor) {
        Optional.ofNullable(fontIconButton.getGraphic())
                .filter(FontIcon.class::isInstance)
                .map(FontIcon.class::cast)
                .ifPresent(fontIcon -> {
                    fontIcon.setIconLiteral(iconLiteral);
                    fontIcon.setIconColor(iconColor);
                });
    }

    public static void manageGetOrCreateFontIcon(final Button fontIconButton, boolean isCreate) {
        Optional.ofNullable(fontIconButton.getGraphic())
                .filter(FontIcon.class::isInstance)
                .map(FontIcon.class::cast)
                .ifPresent(fontIcon -> {
                    fontIcon.setIconLiteral(isCreate ? "fas-plus" : "fas-book");
                    fontIcon.setIconColor(Paint.valueOf(isCreate ? "#43a047" : "#757575"));
                });
    }
}
