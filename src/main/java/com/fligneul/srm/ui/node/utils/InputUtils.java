package com.fligneul.srm.ui.node.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class InputUtils {
    private static final Logger LOGGER = LogManager.getLogger(InputUtils.class);
    public static final String DEFAULT_TIME_FORMAT = "HH:mm";

    /**
     * Parse the raw barcode to the licence number
     *
     * @param input
     *         the barcode
     * @return the licence number
     * @throws IllegalArgumentException
     *         if the input format is not valid
     */
    public static String parseLicenceNumber(final String input) throws IllegalArgumentException {
        Pattern p = Pattern.compile("^(\\d+)(\\W(\\d{4})(\\W(\\d{4}))?)?$");
        Matcher m = p.matcher(input);

        if (!m.find()) {
            throw new IllegalArgumentException("Input format invalid");
        }

        return m.group(1);
    }

    public static boolean isTimeValid(final String input) {
        return isTimeValid(input, DEFAULT_TIME_FORMAT);
    }

    public static boolean isTimeValid(final String input, final String format) {
        try {
            DateTimeFormatter.ofPattern(format).parse(input);
            return true;
        } catch (DateTimeParseException e) {
            LOGGER.debug("Time format invalid", e);
            return false;
        }
    }

    public static boolean areTimesValid(final String inputStart, final String inputEnd) {
        return areTimesValid(inputStart, inputEnd, DEFAULT_TIME_FORMAT);
    }

    public static boolean areTimesValid(final String inputStart, final String inputEnd, final String format) {
        return isTimeValid(inputStart, format) && isTimeValid(inputEnd, format)
                && DateTimeFormatter.ofPattern(format).parse(inputStart).get(ChronoField.MINUTE_OF_DAY) <
                DateTimeFormatter.ofPattern(format).parse(inputEnd).get(ChronoField.MINUTE_OF_DAY);
    }
}
