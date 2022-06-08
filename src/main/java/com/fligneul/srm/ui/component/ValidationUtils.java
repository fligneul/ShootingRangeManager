package com.fligneul.srm.ui.component;

import javafx.util.converter.LocalTimeStringConverter;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.TIME_FORMATTER;

/**
 * Validation utility class
 */
public class ValidationUtils {
    private static final LocalTimeStringConverter TIME_STRING_CONVERTER = new LocalTimeStringConverter(TIME_FORMATTER, TIME_FORMATTER);

    /**
     * @return a validation function for a required not blank string
     */
    public static Function<String, String> validateRequiredString() {
        return value -> {
            if (value == null || value.isBlank()) {
                return null;
            } else {
                return value;
            }
        };
    }

    /**
     * @return a validation function for a required not null integer
     */
    public static Function<String, Integer> validateRequiredInteger() {
        return value -> {
            if (value == null || value.isBlank()) {
                return null;
            } else {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        };
    }

    /**
     * @return a validation function for a required not null {@link LocalTime}
     */
    public static Function<String, LocalTime> validateRequiredTime() {
        return value -> {
            if (value == null || value.isBlank()) {
                return null;
            } else {
                try {
                    return TIME_STRING_CONVERTER.fromString(value);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        };
    }

    /**
     * @return a validation function for a required phone number
     */
    public static Function<String, String> validateRequiredPhoneNumber() {
        return value ->
                Optional.ofNullable(validateRequiredString().apply(value))
                        .filter(input -> {
                            Pattern p = Pattern.compile("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$");
                            Matcher m = p.matcher(input);

                            return m.find();
                        })
                        .orElse(null);
    }

    /**
     * @return a validation function for a required email address
     */
    public static Function<String, String> validateRequiredEmail() {
        return value -> Optional.ofNullable(validateRequiredString().apply(value))
                .filter(input -> EmailValidator.getInstance().isValid(input))
                .orElse(null);
    }

}
