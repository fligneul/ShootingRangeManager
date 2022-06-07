package com.fligneul.srm.ui.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.TextField;

import java.util.Optional;
import java.util.function.Function;

/**
 * A custom text field picker with user input validation
 *
 * @param <T>
 *         the object represented by the text field
 */
public class ValidatedTextField<T> extends TextField {
    private static final PseudoClass ERROR_PSEUDO_CLASS_STATE = PseudoClass.getPseudoClass("error");
    private static final String VALIDATED_TEXT_FIELD_STYLE = "validated-text-field";

    private final BooleanProperty isValidProperty = new SimpleBooleanProperty(false);
    private final ObjectProperty<T> validValueProperty = new SimpleObjectProperty<>();

    private Function<String, T> validator;

    /**
     * Create a validated text field
     */
    public ValidatedTextField() {
        super();
        getStyleClass().add(VALIDATED_TEXT_FIELD_STYLE);
        pseudoClassStateChanged(ERROR_PSEUDO_CLASS_STATE, true);
        isValidProperty.addListener((obs, oldV, newV) -> pseudoClassStateChanged(ERROR_PSEUDO_CLASS_STATE, !newV));

        textProperty().addListener((obs, oldV, newV) -> validateInput(newV));
    }

    private void validateInput(final String inputString) {
        Optional.ofNullable(validator)
                .flatMap(v -> Optional.ofNullable(v.apply(inputString)))
                .ifPresentOrElse(validValue -> {
                    validValueProperty.set(validValue);
                    isValidProperty.set(true);
                }, () -> {
                    validValueProperty.set(null);
                    isValidProperty.set(false);
                });
    }

    /**
     * Set the text field validator and conversion function
     * This function should return {@code null} if the value is not correct and the converted value of type <T>
     *
     * @param validator
     *         the validator and conversion function.
     */
    public void setValidator(Function<String, T> validator) {
        this.validator = validator;
        validateInput(getText());
    }

    /**
     * @return the control valid value
     */
    public T getValidValue() {
        return validValueProperty.get();
    }

    /**
     * @return an observable value of the control valid value
     */
    public ObjectProperty<T> validValueProperty() {
        return validValueProperty;
    }

    /**
     * @return an observable value of the valid state
     */
    public BooleanProperty isValidProperty() {
        return isValidProperty;
    }
}
