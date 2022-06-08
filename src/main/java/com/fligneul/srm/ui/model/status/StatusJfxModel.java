package com.fligneul.srm.ui.model.status;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StatusJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final StringProperty name = new SimpleStringProperty("");


    protected StatusJfxModel(int id, String name) {
        this.id = id;
        this.name.set(name);
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
}
