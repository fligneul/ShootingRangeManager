package com.fligneul.srm.ui.converter;

import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import javafx.util.StringConverter;

import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * String converter for {@link FiringPointJfxModel} to String
 * FromString is not implemented
 */
public class FiringPointConverter extends StringConverter<FiringPointJfxModel> {
    @Override
    public String toString(final FiringPointJfxModel firingPointJfxModel) {
        return Optional.ofNullable(firingPointJfxModel).map(FiringPointJfxModel::getName).orElse(EMPTY);
    }

    @Override
    public FiringPointJfxModel fromString(final String s) {
        throw new IllegalArgumentException("Should not pass here");
    }
}