package com.fligneul.srm.ui.component.cell;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ButtonActionTableCell<T> extends TableCell<T, T> {
    private static final double BUTTON_SIZE = 26;
    private static final int ICON_SIZE = 18;
    private final Button button = new Button();
    @Nullable
    private final Predicate<T> disableCondition;

    public ButtonActionTableCell(final String iconCode, final Paint color, final Consumer<T> onAction, @Nullable final Predicate<T> disableCondition) {
        this.disableCondition = disableCondition;

        button.setMinHeight(BUTTON_SIZE);
        button.setMaxHeight(BUTTON_SIZE);
        button.setMinWidth(BUTTON_SIZE);
        button.setMaxWidth(BUTTON_SIZE);
        FontIcon fontIcon = new FontIcon(iconCode);
        fontIcon.setIconColor(color);
        fontIcon.setIconSize(ICON_SIZE);
        button.setGraphic(fontIcon);

        button.setOnAction((ActionEvent event) -> onAction.accept(getItem()));
    }

    public ButtonActionTableCell(final String iconCode, final Paint color, final Consumer<T> onAction) {
        this(iconCode, color, onAction, null);
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {
            setGraphic(button);
            Optional.ofNullable(disableCondition).ifPresent(condition -> setDisable(condition.test(item)));
        }
    }
}

