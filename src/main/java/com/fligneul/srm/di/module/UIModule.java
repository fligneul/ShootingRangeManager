package com.fligneul.srm.di.module;

import com.fligneul.srm.ui.node.settings.items.AccountSettingsNode;
import com.fligneul.srm.ui.node.settings.items.FiringPointSettingsNode;
import com.fligneul.srm.ui.node.settings.items.UsersSettingsNode;
import com.fligneul.srm.ui.service.licensee.LicenseeSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import com.fligneul.srm.ui.service.range.FiringPointServiceToJfxModel;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * UI injection module
 */
public class UIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FiringPointServiceToJfxModel.class).in(Singleton.class);

        bind(LicenseeServiceToJfxModel.class).in(Singleton.class);

        bind(LicenseeSelectionService.class).in(Singleton.class);

        bind(FiringPointSettingsNode.class).in(Singleton.class);
        bind(AccountSettingsNode.class).in(Singleton.class);
        bind(UsersSettingsNode.class).in(Singleton.class);
    }
}
