package com.fligneul.srm.ui.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidatedDatePicker extends DatePicker {
    private static final PseudoClass ERROR_PSEUDO_CLASS_STATE = PseudoClass.getPseudoClass("error");
    private static final String VALIDATED_TEXT_FIELD_STYLE = "validated-date-picker";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final BooleanProperty isValidProperty = new SimpleBooleanProperty(false);


    public ValidatedDatePicker() {
        super();
        getStyleClass().add(VALIDATED_TEXT_FIELD_STYLE);
        pseudoClassStateChanged(ERROR_PSEUDO_CLASS_STATE, true);
        isValidProperty.addListener((obs, oldV, newV) -> pseudoClassStateChanged(ERROR_PSEUDO_CLASS_STATE, !newV));

        getEditor().textProperty().addListener((obs, oldV, newV) -> validateInput(newV));
    }

    private void validateInput(final String inputString) {
        try {
            if (inputString.isEmpty()) {
                isValidProperty.set(false);
                return;
            }
            setValue(LocalDate.parse(inputString, DATE_FORMATTER));
            isValidProperty.set(true);
        } catch (DateTimeParseException e) {
            isValidProperty.set(false);
        }
    }

    private boolean isValid() {
        return isValidProperty.get();
    }

    public BooleanProperty isValidProperty() {
        return isValidProperty;
    }
}
