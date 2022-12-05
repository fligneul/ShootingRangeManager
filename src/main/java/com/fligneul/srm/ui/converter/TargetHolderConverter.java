package com.fligneul.srm.ui.converter;

import com.fligneul.srm.ui.model.range.TargetHolderJfxModel;
import javafx.util.StringConverter;

import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * String converter for {@link TargetHolderJfxModel} to String
 * FromString is not implemented
 */
public class TargetHolderConverter extends StringConverter<TargetHolderJfxModel> {

    @Override
    public String toString(final TargetHolderJfxModel targetHolderJfxModel) {
        return Optional.ofNullable(targetHolderJfxModel).map(TargetHolderJfxModel::getName).orElse(EMPTY);
    }

    @Override
    public TargetHolderJfxModel fromString(final String s) {
        throw new IllegalArgumentException("Should not pass here");
    }
}
