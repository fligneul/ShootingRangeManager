package com.fligneul.srm.ui.converter;

import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
import javafx.util.StringConverter;

import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * String converter for {@link FiringPostJfxModel} to String
 * FromString is not implemented
 */
public class FiringPostConverter extends StringConverter<FiringPostJfxModel> {
    @Override
    public String toString(final FiringPostJfxModel firingPostJfxModel) {
        return Optional.ofNullable(firingPostJfxModel).map(FiringPostJfxModel::getName).orElse(EMPTY);
    }

    @Override
    public FiringPostJfxModel fromString(final String s) {
        throw new IllegalArgumentException("Should not pass here");
    }
}