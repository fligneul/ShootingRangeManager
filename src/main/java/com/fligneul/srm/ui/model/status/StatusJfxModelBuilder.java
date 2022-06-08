package com.fligneul.srm.ui.model.status;

import java.util.Optional;

public class StatusJfxModelBuilder {
    private Integer id;
    private String name;

    public StatusJfxModelBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    public StatusJfxModelBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public StatusJfxModel createStatusJfxModel() {
        return new StatusJfxModel(Optional.ofNullable(id).orElse(StatusJfxModel.DEFAULT_ID), name);
    }
}
