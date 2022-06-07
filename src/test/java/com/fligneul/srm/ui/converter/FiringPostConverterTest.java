package com.fligneul.srm.ui.converter;

import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FiringPostConverterTest {
    private final FiringPostConverter firingPostConverter = new FiringPostConverter();

    @Test
    void testToString() {
        FiringPostJfxModel firingPostJfxModel = new FiringPostJfxModel(0, "TEST_NAME");
        assertEquals("TEST_NAME", firingPostConverter.toString(firingPostJfxModel));

        assertEquals("", firingPostConverter.toString(null));
    }

    @Test
    void fromString() {
        assertThrows(IllegalArgumentException.class, () -> firingPostConverter.fromString("TEST"));
    }
}
