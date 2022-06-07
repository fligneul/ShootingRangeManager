package com.fligneul.srm.ui.converter;

import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModelBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeaponConverterTest {
    private final WeaponConverter weaponConverter = new WeaponConverter();

    @Test
    void testToString() {
        WeaponJfxModel weaponJfxModel = (new WeaponJfxModelBuilder())
                .setId(0)
                .setName("TEST_NAME")
                .setIdentificationNumber(42)
                .createWeaponJfxModel();
        assertEquals("42 - TEST_NAME", weaponConverter.toString(weaponJfxModel));

        assertEquals("", weaponConverter.toString(null));

    }

    @Test
    void fromString() {
        assertThrows(IllegalArgumentException.class, () -> weaponConverter.fromString("TEST"));
    }
}
