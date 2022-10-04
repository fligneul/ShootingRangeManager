package com.fligneul.srm.ui.node.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @Test
    void containsIgnoreCase() {
        assertTrue(StringUtils.containsIgnoreCase("AZERTY", "AZE"));
        assertTrue(StringUtils.containsIgnoreCase("AZERTY", "TY"));
        assertTrue(StringUtils.containsIgnoreCase("AZERTY", ""));

        assertFalse(StringUtils.containsIgnoreCase(null, "TY"));
        assertFalse(StringUtils.containsIgnoreCase("", "TY"));
        assertFalse(StringUtils.containsIgnoreCase("AZERTY", "ZET"));

        assertThrows(NullPointerException.class, () -> StringUtils.containsIgnoreCase("AZERTY", null));
    }
}