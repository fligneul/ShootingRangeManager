package com.fligneul.srm.ui.converter;

import com.fligneul.srm.ui.model.range.CaliberJfxModel;
import javafx.util.StringConverter;

import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * String converter for {@link CaliberJfxModel} to String
 * FromString is not implemented
 */
public class CaliberConverter extends StringConverter<CaliberJfxModel> {

    @Override
    public String toString(final CaliberJfxModel caliberJfxModel) {
        return Optional.ofNullable(caliberJfxModel).map(CaliberJfxModel::getName).orElse(EMPTY);
    }

    @Override
    public CaliberJfxModel fromString(final String s) {
        throw new IllegalArgumentException("Should not pass here");
    }
}
