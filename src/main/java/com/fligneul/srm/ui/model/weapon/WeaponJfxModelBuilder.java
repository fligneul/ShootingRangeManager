package com.fligneul.srm.ui.model.weapon;

import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Weapon model builder for JavaFX views
 */
public class WeaponJfxModelBuilder {
    private Integer id;
    private String name;
    private Integer identificationNumber;
    private String caliber;
    private LocalDate buyDate;
    private ObservableList<FiringPointJfxModel> availableFiringPoint;

    public WeaponJfxModelBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    public WeaponJfxModelBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public WeaponJfxModelBuilder setIdentificationNumber(final Integer identificationNumber) {
        this.identificationNumber = identificationNumber;
        return this;
    }

    public WeaponJfxModelBuilder setCaliber(final String caliber) {
        this.caliber = caliber;
        return this;
    }

    public WeaponJfxModelBuilder setBuyDate(final LocalDate buyDate) {
        this.buyDate = buyDate;
        return this;
    }

    public WeaponJfxModelBuilder setAvailableFiringPoint(final ObservableList<FiringPointJfxModel> availableFiringPoint) {
        this.availableFiringPoint = availableFiringPoint;
        return this;
    }

    public WeaponJfxModel createWeaponJfxModel() {
        return new WeaponJfxModel(Optional.ofNullable(id).orElse(WeaponJfxModel.DEFAULT_ID), name, identificationNumber, caliber, buyDate, availableFiringPoint);
    }
}
