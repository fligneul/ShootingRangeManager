package com.fligneul.srm.ui.model.weapon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class WeaponJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final StringProperty name = new SimpleStringProperty("");
    private final IntegerProperty identificationNumber = new SimpleIntegerProperty();
    private final StringProperty caliber = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> buyDate = new SimpleObjectProperty<>(LocalDate.EPOCH);


    protected WeaponJfxModel(int id, String name, Integer identificationNumber, String caliber, LocalDate buyDate) {
        this.id = id;
        this.name.set(name);
        this.identificationNumber.set(identificationNumber);
        this.caliber.set(caliber);
        this.buyDate.set(buyDate);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public ReadOnlyStringProperty nameProperty() {
        return name;
    }

    public void setName(final String name) {
        this.name.set(name);
    }

    public int getIdentificationNumber() {
        return identificationNumber.get();
    }

    public ReadOnlyIntegerProperty identificationNumberProperty() {
        return identificationNumber;
    }

    private void setIdentificationNumber(final int identificationNumber) {
        this.identificationNumber.set(identificationNumber);
    }

    public String getCaliber() {
        return caliber.get();
    }

    public ReadOnlyStringProperty caliberProperty() {
        return caliber;
    }

    public void setCaliber(final String caliber) {
        this.caliber.set(caliber);
    }

    public LocalDate getBuyDate() {
        return buyDate.get();
    }

    public ReadOnlyObjectProperty<LocalDate> buyDateProperty() {
        return buyDate;
    }

    public void setBuyDate(final LocalDate buyDate) {
        this.buyDate.set(buyDate);
    }
}
