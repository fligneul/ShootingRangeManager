package com.fligneul.srm.ui.model.logbook;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Objects;

public class ShootingLogbookJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final ObjectProperty<LocalDate> creationDate = new SimpleObjectProperty<>();
    private final ListProperty<ShootingSessionJfxModel> sessions = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ShootingLogbookJfxModel(int id, LocalDate creationDate) {
        this.id = id;
        this.creationDate.set(creationDate);
    }

    public ShootingLogbookJfxModel(LocalDate creationDate) {
        this(DEFAULT_ID, creationDate);
    }

    public ShootingLogbookJfxModel(int id, LocalDate creationDate, ObservableList<ShootingSessionJfxModel> sessions) {
        this(id, creationDate);
        this.sessions.set(this.sessions);
    }

    public int getId() {
        return id;
    }

    public LocalDate getCreationDate() {
        return creationDate.get();
    }

    public ObjectProperty<LocalDate> creationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate.set(creationDate);
    }

    public ObservableList<ShootingSessionJfxModel> getSessions() {
        return sessions.get();
    }

    public ListProperty<ShootingSessionJfxModel> sessionsProperty() {
        return sessions;
    }

    public void setSessions(ObservableList<ShootingSessionJfxModel> sessions) {
        this.sessions.set(sessions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShootingLogbookJfxModel that = (ShootingLogbookJfxModel) o;
        return id == that.id && creationDate.equals(that.creationDate) && sessions.equals(that.sessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, sessions);
    }
}
