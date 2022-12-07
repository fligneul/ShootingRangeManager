package com.fligneul.srm.ui.converter;

import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModelBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatusConverterTest {
    private final StatusConverter statusConverter = new StatusConverter();

    @Test
    void testToString() {
        StatusJfxModel statusJfxModel = (new StatusJfxModelBuilder())
                .setId(0)
                .setName("TEST_NAME")
                .createStatusJfxModel();
        assertEquals("TEST_NAME", statusConverter.toString(statusJfxModel));

        assertEquals("", statusConverter.toString(null));

    }

    @Test
    void fromString() {
        assertThrows(IllegalArgumentException.class, () -> statusConverter.fromString("TEST"));
    }
}
