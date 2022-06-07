package com.fligneul.srm.ui.model.user;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * User model for JavaFX views
 */
public class UserJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final StringProperty name = new SimpleStringProperty(EMPTY);
    private final ObjectProperty<ERole> role = new SimpleObjectProperty<>();

    public UserJfxModel(int id, String name) {
        this.id = id;
        this.name.set(name);
    }

    public UserJfxModel(String name) {
        this(DEFAULT_ID, name);
    }

    public UserJfxModel(int id, String name, ERole role) {
        this(id, name);
        this.role.set(role);
    }

    public UserJfxModel(String name, ERole role) {
        this(DEFAULT_ID, name);
        this.role.set(role);
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

    public ERole getRole() {
        return role.get();
    }

    public ObjectProperty<ERole> roleProperty() {
        return role;
    }

    public void setName(ERole role) {
        this.role.set(role);
    }

}
