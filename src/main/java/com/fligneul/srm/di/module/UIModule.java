package com.fligneul.srm.di.module;

import com.fligneul.srm.ui.node.settings.items.AccountSettingsNode;
import com.fligneul.srm.ui.node.settings.items.FiringPointSettingsNode;
import com.fligneul.srm.ui.node.settings.items.GeneralSettingsNode;
import com.fligneul.srm.ui.node.settings.items.StatusSettingsNode;
import com.fligneul.srm.ui.node.settings.items.UsersSettingsNode;
import com.fligneul.srm.ui.node.settings.items.WeaponSettingsNode;
import com.fligneul.srm.ui.service.attendance.AttendanceSelectionService;
import com.fligneul.srm.ui.service.attendance.AttendanceServiceToJfxModel;
import com.fligneul.srm.ui.service.history.HistoryAttendanceServiceToJfxModel;
import com.fligneul.srm.ui.service.licensee.LicenseeSelectionService;
import com.fligneul.srm.ui.service.licensee.LicenseeServiceToJfxModel;
import com.fligneul.srm.ui.service.licensee.ProfilePictureService;
import com.fligneul.srm.ui.service.logbook.ShootingLogbookServiceToJfxModel;
import com.fligneul.srm.ui.service.range.FiringPointServiceToJfxModel;
import com.fligneul.srm.ui.service.status.StatusServiceToJfxModel;
import com.fligneul.srm.ui.service.user.UserViewService;
import com.fligneul.srm.ui.service.weapon.WeaponServiceToJfxModel;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;

/**
 * UI injection module
 */
public class UIModule extends AbstractModule {

    public static final String DEFAULT_PICTURE_PATH_INJECT = "DEFAULT_PICTURE_PATH";
    public static final String PICTURE_DIRECTORY_PATH_INJECT = "PICTURE_DIRECTORY_PATH";

    @Override
    protected void configure() {
        bind(UserViewService.class).in(Singleton.class);
        bind(ProfilePictureService.class).in(Singleton.class);

        bind(WeaponServiceToJfxModel.class).in(Singleton.class);
        bind(StatusServiceToJfxModel.class).in(Singleton.class);
        bind(FiringPointServiceToJfxModel.class).in(Singleton.class);
        bind(LicenseeServiceToJfxModel.class).in(Singleton.class);
        bind(AttendanceServiceToJfxModel.class).in(Singleton.class);
        bind(HistoryAttendanceServiceToJfxModel.class).in(Singleton.class);
        bind(ShootingLogbookServiceToJfxModel.class).in(Singleton.class);

        bind(LicenseeSelectionService.class).in(Singleton.class);
        bind(AttendanceSelectionService.class).in(Singleton.class);

        bind(GeneralSettingsNode.class).in(Singleton.class);
        bind(FiringPointSettingsNode.class).in(Singleton.class);
        bind(AccountSettingsNode.class).in(Singleton.class);
        bind(UsersSettingsNode.class).in(Singleton.class);
        bind(WeaponSettingsNode.class).in(Singleton.class);
        bind(StatusSettingsNode.class).in(Singleton.class);
    }

    @Provides
    @Named(DEFAULT_PICTURE_PATH_INJECT)
    private String provideDefaultPicturePath() {
        return "/com/fligneul/srm/image/empty-picture.png";
    }

    @Provides
    @Named(PICTURE_DIRECTORY_PATH_INJECT)
    private Path providePictureDirectoryPath() {
        return Path.of(System.getenv("APPDATA"), "ShootingRangeManager", "pictures");
    }

}
