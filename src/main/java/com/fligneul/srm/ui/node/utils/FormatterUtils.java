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

public class FormatterUtils {
    public static String formatDate(@Nullable final LocalDate date) {
        return formatDate(date, EMPTY);
    }

    public static String formatDate(@Nullable final LocalDate date, final String emptyDisplay) {
        return Optional.ofNullable(date).map(d -> d.format(DATE_FORMATTER)).orElse(emptyDisplay);
    }

    public static String formatTime(@Nullable final LocalDateTime time) {
        return formatTime(time, EMPTY);
    }

    public static String formatTime(@Nullable final LocalDateTime time, final String emptyDisplay) {
        return Optional.ofNullable(time).map(t -> t.format(TIME_FORMATTER)).orElse(emptyDisplay);
    }

    public static String formatLicenseeName(@Nonnull final LicenseeJfxModel licenseeJfxModel) {
        StringJoiner nameJoiner = new StringJoiner(SPACE);
        Optional.ofNullable(licenseeJfxModel.getFirstName()).ifPresent(nameJoiner::add);
        Optional.ofNullable(licenseeJfxModel.getLastName()).ifPresent(nameJoiner::add);
        return nameJoiner.toString();
    }
}
