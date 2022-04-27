package com.fligneul.srm.ui.model.range;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * Firing post model for JavaFX views
 */
public class FiringPostJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final StringProperty name = new SimpleStringProperty(EMPTY);
    private final BooleanProperty occupied = new SimpleBooleanProperty(false);

    public FiringPostJfxModel(int id, String name) {
        this.id = id;
        this.name.set(name);
    }

    public FiringPostJfxModel(String name) {
        this(DEFAULT_ID, name);
    }

    public FiringPostJfxModel(int id, String name, Boolean occupied) {
        this(id, name);
        this.occupied.set(occupied);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isOccupied() {
        return occupied.get();
    }

    public BooleanProperty occupiedProperty() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied.set(occupied);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FiringPostJfxModel that = (FiringPostJfxModel) o;
        return getId() == that.getId() && getName().equals(that.getName()) && isOccupied() == that.isOccupied();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), isOccupied());
    }
}
