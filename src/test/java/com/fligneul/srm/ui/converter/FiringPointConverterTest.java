package com.fligneul.srm.ui.converter;

import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FiringPointConverterTest {

    private final FiringPointConverter firingPointConverter = new FiringPointConverter();

    @Test
    void testToString() {
        FiringPointJfxModel firingPointJfxModel = new FiringPointJfxModel("TEST_NAME");
        assertEquals("TEST_NAME", firingPointConverter.toString(firingPointJfxModel));

        assertEquals("", firingPointConverter.toString(null));
    }

    @Test
    void fromString() {
        assertThrows(IllegalArgumentException.class, () -> firingPointConverter.fromString("TEST"));
    }
}
