package com.fligneul.srm.di.module;

import com.fligneul.srm.ui.node.settings.items.AccountSettingsNode;
import com.fligneul.srm.ui.node.settings.items.FiringPointSettingsNode;
import com.fligneul.srm.ui.node.settings.items.GeneralSettingsNode;
import com.fligneul.srm.ui.node.settings.items.UsersSettingsNode;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import com.fligneul.srm.ui.service.attendance.AttendanceServiceToJfxModel;
import com.fligneul.srm.ui.service.licensee.LicenseeSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import com.fligneul.srm.ui.service.range.FiringPointServiceToJfxModel;
import com.fligneul.srm.ui.service.user.UserViewService;
import com.fligneul.srm.ui.service.weapon.WeaponServiceToJfxModel;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * UI injection module
 */
public class UIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserViewService.class).in(Singleton.class);

        bind(WeaponServiceToJfxModel.class).in(Singleton.class);
        bind(FiringPointServiceToJfxModel.class).in(Singleton.class);
        bind(LicenseeServiceToJfxModel.class).in(Singleton.class);
        bind(AttendanceServiceToJfxModel.class).in(Singleton.class);

        bind(LicenseeSelectionService.class).in(Singleton.class);
        bind(AttendanceSelectionService.class).in(Singleton.class);

        bind(GeneralSettingsNode.class).in(Singleton.class);
        bind(FiringPointSettingsNode.class).in(Singleton.class);
        bind(AccountSettingsNode.class).in(Singleton.class);
        bind(UsersSettingsNode.class).in(Singleton.class);
    }
}
