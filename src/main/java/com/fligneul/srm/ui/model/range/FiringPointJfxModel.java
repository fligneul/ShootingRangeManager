package com.fligneul.srm.ui.model.range;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

public class FiringPointJfxModel {
    public static int DEFAULT_ID = -1;

    private final int id;
    private final StringProperty name = new SimpleStringProperty("");
    private final ListProperty<FiringPostJfxModel> posts = new SimpleListProperty<>(FXCollections.observableArrayList());

    public FiringPointJfxModel(int id, String name) {
        this.id = id;
        this.name.set(name);
    }

    public FiringPointJfxModel(String name) {
        this(DEFAULT_ID, name);
    }

    public FiringPointJfxModel(int id, String name, ObservableList<FiringPostJfxModel> posts) {
        this(id, name);
        this.posts.set(posts);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FiringPointJfxModel that = (FiringPointJfxModel) o;
        return getId() == that.getId() && getName().equals(that.getName()) && getPosts().equals(that.getPosts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPosts());
    }
}
