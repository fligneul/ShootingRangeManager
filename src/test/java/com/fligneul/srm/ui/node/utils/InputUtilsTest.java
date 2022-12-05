package com.fligneul.srm.ui.node.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("ALL")
public class InputUtilsTest {

    @Test
    void parseLicenceNumber() {
        // Valid format
        assertEquals("01234567", InputUtils.parseLicenceNumber("01234567"));
        assertEquals("01234567", InputUtils.parseLicenceNumber("01234567|2021/2022"));
        assertEquals("01234567", InputUtils.parseLicenceNumber("01234567-2021/2022"));
        assertEquals("01234567", InputUtils.parseLicenceNumber("01234567*2021§2022"));
        assertEquals("01234567", InputUtils.parseLicenceNumber("01234567-2022"));


        // Invalid format
        assertThrows(IllegalArgumentException.class, () -> InputUtils.parseLicenceNumber("0123456L"));
    }

    @Test
    void isTimeValid() {
        // Valid format
        assertTrue(InputUtils.isTimeValid("12:00"));
        assertTrue(InputUtils.isTimeValid("12h00", "HH'h'mm"));

        // Invalid format
        assertFalse(InputUtils.isTimeValid("12z40"));
        assertFalse(InputUtils.isTimeValid("12:00", "HH'h'mm"));
    }

    @Test
    void areTimesValid() {
        // Valid format
        assertTrue(InputUtils.areTimesValid("12:00", "13:00"));
        assertTrue(InputUtils.areTimesValid("12h00", "13h00", "HH'h'mm"));

        // Invalid format
        assertFalse(InputUtils.areTimesValid("12z40", "13z40"));
        assertFalse(InputUtils.areTimesValid("12:00", "13:00", "HH'h'mm"));
    }
}
