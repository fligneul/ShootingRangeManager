package com.fligneul.srm.ui.component;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;
import javafx.collections.ObservableList;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Function;

public class ValidationUtils {
    public static final LocalTimeStringConverter TIME_STRING_CONVERTER = new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), DateTimeFormatter.ofPattern("HH:mm"));


    public static Function<String, String> validateRequiredString() {
        return value -> {
            if (value == null || value.isBlank()) {
                return null;
            } else {
                return value;
            }
        };
    }

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

    public static Function<String, String> validateLicenceNumber(final ObservableList<LicenseeJfxModel> licenseeList) {
        return value -> Optional.ofNullable(validateRequiredString().apply(value))
                .map(licenceNb -> licenseeList.stream().map(LicenseeJfxModel::getLicenceNumber).anyMatch(v -> v.equals(licenceNb)))
                .filter(Boolean.TRUE::equals)
                .map(any -> value)
                .orElse(null);
    }
}
