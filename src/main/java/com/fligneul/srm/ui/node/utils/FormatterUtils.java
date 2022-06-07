package com.fligneul.srm.ui.node.utils;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.StringJoiner;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.DATE_FORMATTER;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.SPACE;
import static com.fligneul.srm.ui.ShootingRangeManagerConstants.TIME_FORMATTER;

/**
 * Utility class for text formatting
 */
public class FormatterUtils {
    /**
     * Format an Optional {@link LocalDate} to a short version string or an empty string if null
     *
     * @param date
     *         the nullable {@link LocalDate}
     * @return the formatted date
     */
    public static String formatDate(@Nullable final LocalDate date) {
        return formatDate(date, EMPTY);
    }

    /**
     * Format an Optional {@link LocalDate} to a short version string or the empty display parameter if null
     *
     * @param date
     *         the nullable {@link LocalDate}
     * @param emptyDisplay
     *         string returned if date is null
     * @return the formatted date
     */
    public static String formatDate(@Nullable final LocalDate date, final String emptyDisplay) {
        return Optional.ofNullable(date).map(d -> d.format(DATE_FORMATTER)).orElse(emptyDisplay);
    }

    /**
     * Format an Optional {@link LocalDateTime} to a short version string or an empty string if null
     *
     * @param time
     *         the nullable {@link LocalDateTime}
     * @return the formatted time
     */
    public static String formatTime(@Nullable final LocalDateTime time) {
        return formatTime(time, EMPTY);
    }

    /**
     * Format an Optional {@link LocalDateTime} to a short version string or the empty display parameter if null
     *
     * @param time
     *         the nullable {@link LocalDateTime}
     * @param emptyDisplay
     *         string returned if time is null
     * @return the formatted time
     */
    public static String formatTime(@Nullable final LocalDateTime time, final String emptyDisplay) {
        return Optional.ofNullable(time).map(t -> t.format(TIME_FORMATTER)).orElse(emptyDisplay);
    }

    /**
     * Format an Optional {@link LicenseeJfxModel} to a firstname and lastname description or empty if null
     *
     * @param licenseeJfxModel
     *         licensee to format
     * @return the formatted licensee name
     */
    public static String formatLicenseeName(@Nonnull final LicenseeJfxModel licenseeJfxModel) {
        StringJoiner nameJoiner = new StringJoiner(SPACE);
        Optional.ofNullable(licenseeJfxModel.getFirstName()).ifPresent(nameJoiner::add);
        Optional.ofNullable(licenseeJfxModel.getLastName()).ifPresent(nameJoiner::add);
        return nameJoiner.toString();
    }
}
