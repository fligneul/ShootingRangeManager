package com.fligneul.srm.di.module;

import com.fligneul.srm.dao.attendance.AttendanceDAO;
import com.fligneul.srm.dao.licensee.LicenseeDAO;
import com.fligneul.srm.dao.logbook.ShootingLogbookDAO;
import com.fligneul.srm.dao.logbook.ShootingSessionDAO;
import com.fligneul.srm.dao.range.FiringPointDAO;
import com.fligneul.srm.dao.range.FiringPostDAO;
import com.fligneul.srm.dao.status.StatusDAO;
import com.fligneul.srm.dao.user.UserDAO;
import com.fligneul.srm.dao.weapon.WeaponDAO;
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

        bind(FiringPostDAO.class).in(Singleton.class);
        bind(FiringPointDAO.class).in(Singleton.class);
        bind(WeaponDAO.class).in(Singleton.class);

        bind(ShootingSessionDAO.class).in(Singleton.class);
        bind(ShootingLogbookDAO.class).in(Singleton.class);
        bind(LicenseeDAO.class).in(Singleton.class);
        bind(StatusDAO.class).in(Singleton.class);
        bind(AttendanceDAO.class).in(Singleton.class);
    }
}
