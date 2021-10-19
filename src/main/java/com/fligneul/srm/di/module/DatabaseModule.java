package com.fligneul.srm.di.module;

import com.fligneul.srm.dao.user.UserDAO;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Database injection module
 */
public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DatabaseConnectionService.class).in(Singleton.class);

        bind(UserDAO.class).in(Singleton.class);
    }
}
