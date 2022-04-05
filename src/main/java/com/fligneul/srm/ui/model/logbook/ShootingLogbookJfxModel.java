package com.fligneul.srm.ui.model.logbook;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Objects;

public class ShootingLogbookJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final ObjectProperty<LocalDate> creationDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> knowledgeCheckDate = new SimpleObjectProperty<>();
    private final BooleanProperty whiteTargetLevel = new SimpleBooleanProperty(false);
    private final ListProperty<ShootingSessionJfxModel> sessions = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final int licenseeId;

    public ShootingLogbookJfxModel(int id, LocalDate creationDate, int licenseeId) {
        this.id = id;
        this.creationDate.set(creationDate);
        this.licenseeId = licenseeId;
    }

    public ShootingLogbookJfxModel(LocalDate creationDate, int licenseeId) {
        this(DEFAULT_ID, creationDate, licenseeId);
    }

    public ShootingLogbookJfxModel(int id, LocalDate creationDate, ObservableList<ShootingSessionJfxModel> sessions, int licenseeId) {
        this(id, creationDate, licenseeId);
        this.sessions.set(sessions);
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

    public int getLicenseeId() {
        return licenseeId;
    }

    public @Null LocalDate getKnowledgeCheckDate() {
        return knowledgeCheckDate.get();
    }

    public ObjectProperty<LocalDate> knowledgeCheckDateProperty() {
        return knowledgeCheckDate;
    }

    public void setKnowledgeCheckDate(final LocalDate knowledgeCheckDate) {
        this.knowledgeCheckDate.set(knowledgeCheckDate);
    }

    public boolean hasWhiteTargetLevel() {
        return whiteTargetLevel.get();
    }

    public BooleanProperty whiteTargetLevelProperty() {
        return whiteTargetLevel;
    }

    public void setWhiteTargetLevel(final boolean hasWhiteTargetLevel) {
        this.whiteTargetLevel.set(hasWhiteTargetLevel);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ShootingLogbookJfxModel that = (ShootingLogbookJfxModel) o;
        return getId() == that.getId() && getLicenseeId() == that.getLicenseeId() && Objects.equals(getCreationDate(), that.getCreationDate()) && Objects.equals(getKnowledgeCheckDate(), that.getKnowledgeCheckDate()) && Objects.equals(whiteTargetLevel, that.whiteTargetLevel) && Objects.equals(getSessions(), that.getSessions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreationDate(), getKnowledgeCheckDate(), whiteTargetLevel, getSessions(), getLicenseeId());
    }
}
