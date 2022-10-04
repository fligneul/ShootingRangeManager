package com.fligneul.srm.ui.node.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Utility class for string validation
 */
public class StringUtils {

    /**
     * Test if the source string contains the provided value
     *
     * @param source
     *         source string
     * @param str
     *         string to search
     * @return true if {@code source} contains {@code str} ignoring case
     */
    public static boolean containsIgnoreCase(@Nullable final String source, @Nonnull final String str) {
        return Optional.ofNullable(source)
                .map(String::toUpperCase)
                .map(src -> src.contains(str.toUpperCase()))
                .orElse(Boolean.FALSE);
    }

}
