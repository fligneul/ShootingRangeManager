package com.fligneul.srm.ui.model.logbook;

import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class ShootingSessionJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final ObjectProperty<LocalDate> sessionDate = new SimpleObjectProperty<>();
    private final StringProperty instructorName = new SimpleStringProperty("");
    private final ObjectProperty<WeaponJfxModel> weapon = new SimpleObjectProperty<>();

    public ShootingSessionJfxModel(int id, LocalDate sessionDate, String instructorName) {
        this.id = id;
        this.sessionDate.set(sessionDate);
        this.instructorName.set(instructorName);
    }

    public ShootingSessionJfxModel(LocalDate sessionDate, String instructorName) {
        this(DEFAULT_ID, sessionDate, instructorName);
    }

    public int getId() {
        return id;
    }

    public LocalDate getSessionDate() {
        return sessionDate.get();
    }

    public ObjectProperty<LocalDate> sessionDateProperty() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate.set(sessionDate);
    }

    public String getInstructorName() {
        return instructorName.get();
    }

    public StringProperty instructorNameProperty() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName.set(instructorName);
    }

    public Optional<WeaponJfxModel> getWeapon() {
        return Optional.ofNullable(weapon.get());
    }

    public ObjectProperty<WeaponJfxModel> weaponProperty() {
        return weapon;
    }

    public void setWeapon(WeaponJfxModel weapon) {
        this.weapon.set(weapon);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShootingSessionJfxModel that = (ShootingSessionJfxModel) o;
        return id == that.id && sessionDate.equals(that.sessionDate) && instructorName.equals(that.instructorName) && Objects.equals(weapon, that.weapon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionDate, instructorName, weapon);
    }
}
