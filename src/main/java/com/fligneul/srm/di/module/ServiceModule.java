package com.fligneul.srm.di.module;

import com.fligneul.srm.service.AuthenticationService;
import com.fligneul.srm.service.BackupService;
import com.fligneul.srm.service.FirstStartService;
import com.fligneul.srm.service.PreferenceService;
import com.fligneul.srm.service.RoleService;
import com.fligneul.srm.service.ShutdownService;
import com.fligneul.srm.service.UserService;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Service injection module
 */
public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PreferenceService.class).in(Singleton.class);
        bind(FirstStartService.class).in(Singleton.class);

        bind(UserService.class).in(Singleton.class);
        bind(RoleService.class).in(Singleton.class);
        bind(AuthenticationService.class).in(Singleton.class);
        bind(ShutdownService.class).in(Singleton.class);

        bind(BackupService.class).in(Singleton.class);
    }
}
