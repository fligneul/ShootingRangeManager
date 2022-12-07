package com.fligneul.srm.ui.model.range;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

import static com.fligneul.srm.ui.ShootingRangeManagerConstants.EMPTY;

/**
 * Firing point model for JavaFX views
 */
public class FiringPointJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final StringProperty name = new SimpleStringProperty(EMPTY);
    private final ListProperty<FiringPostJfxModel> posts = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<TargetHolderJfxModel> targetHolders = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<CaliberJfxModel> calibers = new SimpleListProperty<>(FXCollections.observableArrayList());

    public FiringPointJfxModel(int id, String name) {
        this.id = id;
        this.name.set(name);
    }

    public FiringPointJfxModel(String name) {
        this(DEFAULT_ID, name);
    }

    public FiringPointJfxModel(int id, String name, ObservableList<FiringPostJfxModel> posts, ObservableList<TargetHolderJfxModel> targetHolders, ObservableList<CaliberJfxModel> calibers) {
        this(id, name);
        this.posts.set(posts);
        this.targetHolders.set(targetHolders);
        this.calibers.set(calibers);
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

    public ObservableList<FiringPostJfxModel> getPosts() {
        return posts.get();
    }

    public ListProperty<FiringPostJfxModel> postsProperty() {
        return posts;
    }

    public void setPosts(ObservableList<FiringPostJfxModel> posts) {
        this.posts.set(posts);
    }

    public ObservableList<TargetHolderJfxModel> getTargetHolders() {
        return targetHolders.get();
    }

    public ListProperty<TargetHolderJfxModel> targetHoldersProperty() {
        return targetHolders;
    }

    public void setTargetHolders(ObservableList<TargetHolderJfxModel> targetHolders) {
        this.targetHolders.set(targetHolders);
    }

    public ObservableList<CaliberJfxModel> getCalibers() {
        return calibers.get();
    }

    public ListProperty<CaliberJfxModel> calibersProperty() {
        return calibers;
    }

    public void setCalibers(ObservableList<CaliberJfxModel> calibers) {
        this.calibers.set(calibers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FiringPointJfxModel that = (FiringPointJfxModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(posts, that.posts) && Objects.equals(targetHolders, that.targetHolders) && Objects.equals(calibers, that.calibers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, posts, targetHolders, calibers);
    }
}
