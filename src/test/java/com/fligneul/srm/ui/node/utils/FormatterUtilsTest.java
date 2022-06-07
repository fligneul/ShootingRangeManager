package com.fligneul.srm.ui.node.utils;

import com.fligneul.srm.ui.model.licensee.LicenseeJfxModelBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormatterUtilsTest {

    @Test
    void formatDate() {
        assertEquals("30/04/2022", FormatterUtils.formatDate(LocalDate.of(2022, 4, 30)));
        assertEquals("", FormatterUtils.formatDate(null));
        assertEquals("TEST_EMPTY", FormatterUtils.formatDate(null, "TEST_EMPTY"));
    }

    @Test
    void formatTime() {
        assertEquals("12:34", FormatterUtils.formatTime(LocalDateTime.parse("2022-04-30T12:34:56")));
        assertEquals("", FormatterUtils.formatTime(null));
        assertEquals("TEST_EMPTY", FormatterUtils.formatTime(null, "TEST_EMPTY"));
    }

    @Test
    void formatLicenseeName() {
        assertEquals("TEST_FIRSTNAME TEST_LASTNAME", FormatterUtils.formatLicenseeName((new LicenseeJfxModelBuilder()).setFirstName("TEST_FIRSTNAME").setLastName("TEST_LASTNAME").createLicenseeJfxModel()));
        assertEquals("TEST_FIRSTNAME", FormatterUtils.formatLicenseeName((new LicenseeJfxModelBuilder()).setFirstName("TEST_FIRSTNAME").createLicenseeJfxModel()));
        assertEquals("TEST_LASTNAME", FormatterUtils.formatLicenseeName((new LicenseeJfxModelBuilder()).setLastName("TEST_LASTNAME").createLicenseeJfxModel()));
        assertEquals("", FormatterUtils.formatLicenseeName((new LicenseeJfxModelBuilder()).createLicenseeJfxModel()));

        assertThrows(NullPointerException.class, () -> FormatterUtils.formatLicenseeName(null));
    }
}
