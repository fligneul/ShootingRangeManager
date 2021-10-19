package com.fligneul.srm.di.module;

import com.fligneul.srm.ui.service.licensee.LicenseeSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * UI injection module
 */
public class UIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(LicenseeServiceToJfxModel.class).in(Singleton.class);

        bind(LicenseeSelectionService.class).in(Singleton.class);
    }
}
