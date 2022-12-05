package com.fligneul.srm.ui.model.range;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * Target holder model for JavaFX views
 */
public class TargetHolderJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final StringProperty name = new SimpleStringProperty(EMPTY);

    public TargetHolderJfxModel(int id, String name) {
        this.id = id;
        this.name.set(name);
    }

    public TargetHolderJfxModel(String name) {
        this(DEFAULT_ID, name);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TargetHolderJfxModel that = (TargetHolderJfxModel) o;
        return getId() == that.getId() && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
