package com.fligneul.srm.ui.model.weapon;

import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * Weapon model for JavaFX views
 */
public class WeaponJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final StringProperty name = new SimpleStringProperty(EMPTY);
    private final IntegerProperty identificationNumber = new SimpleIntegerProperty();
    private final StringProperty caliber = new SimpleStringProperty(EMPTY);
    private final ObjectProperty<LocalDate> buyDate = new SimpleObjectProperty<>(LocalDate.EPOCH);
    private final ListProperty<FiringPointJfxModel> availableFiringPoint = new SimpleListProperty<>(FXCollections.observableArrayList());


    protected WeaponJfxModel(int id, String name, Integer identificationNumber, String caliber, LocalDate buyDate, ObservableList<FiringPointJfxModel> availableFiringPointList) {
        this.id = id;
        this.name.set(name);
        this.identificationNumber.set(identificationNumber);
        this.caliber.set(caliber);
        this.buyDate.set(buyDate);
        this.availableFiringPoint.set(availableFiringPointList);
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

    public ObservableList<FiringPointJfxModel> getAvailableFiringPoint() {
        return availableFiringPoint.get();
    }

    public ListProperty<FiringPointJfxModel> availableFiringPointProperty() {
        return availableFiringPoint;
    }
}
