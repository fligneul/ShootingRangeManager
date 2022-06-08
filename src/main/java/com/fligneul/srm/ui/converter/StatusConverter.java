package com.fligneul.srm.ui.converter;

import com.fligneul.srm.ui.model.status.StatusJfxModel;
import javafx.util.StringConverter;

import java.util.Optional;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * String converter for {@link StatusJfxModel} to String
 * FromString is not implemented
 */
public class StatusConverter extends StringConverter<StatusJfxModel> {

    @Override
    public String toString(final StatusJfxModel statusJfxModel) {
        return Optional.ofNullable(statusJfxModel).map(StatusJfxModel::getName).orElse(EMPTY);
    }

    @Override
    public StatusJfxModel fromString(final String s) {
        throw new IllegalArgumentException("Should not pass here");
    }
}