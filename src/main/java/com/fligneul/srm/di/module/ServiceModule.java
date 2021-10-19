package com.fligneul.srm.di.module;

import com.fligneul.srm.service.AuthenticationService;
import com.fligneul.srm.service.FirstStartService;
import com.fligneul.srm.service.ShutdownService;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Service injection module
 */
public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FirstStartService.class).in(Singleton.class);

        bind(AuthenticationService.class).in(Singleton.class);
        bind(ShutdownService.class).in(Singleton.class);
    }
}
