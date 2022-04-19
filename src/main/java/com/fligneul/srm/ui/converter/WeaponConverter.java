package com.fligneul.srm.ui.converter;

import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import javafx.util.StringConverter;

import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * String converter for {@link WeaponJfxModel} to String
 * FromString is not implemented
 */
public class WeaponConverter extends StringConverter<WeaponJfxModel> {

    private static final String WEAPON_DESCRIPTION_SEPARATOR = " - ";

    @Override
    public String toString(final WeaponJfxModel weaponJfxModel) {
        return Optional.ofNullable(weaponJfxModel).map(model -> model.getIdentificationNumber() + WEAPON_DESCRIPTION_SEPARATOR + model.getName()).orElse(EMPTY);
    }

    @Override
    public WeaponJfxModel fromString(final String s) {
        throw new IllegalArgumentException("Should not pass here");
    }
}