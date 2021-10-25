package com.fligneul.srm.dao.weapon;

import com.fligneul.srm.dao.IDAO;
import com.fligneul.srm.jooq.Tables;
import com.fligneul.srm.jooq.tables.records.WeaponRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Condition;
import org.jooq.exception.DataAccessException;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeaponDAO implements IDAO<WeaponJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(WeaponDAO.class);

    private DatabaseConnectionService databaseConnectionService;

    @Inject
    public void injectDependencies(final DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    @Override
    public Optional<WeaponJfxModel> save(final WeaponJfxModel item) {
        try {
            Optional<WeaponJfxModel> optLicensee = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.WEAPON)
                            .set(Tables.WEAPON.NAME, item.getName())
                            .set(Tables.WEAPON.IDENTIFICATIONNUMBER, item.getIdentificationNumber())
                            .set(Tables.WEAPON.CALIBER, item.getCaliber())
                            .set(Tables.WEAPON.BUYDATE, item.getBuyDate())
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            optLicensee.ifPresentOrElse(saved -> LOGGER.debug("Weapon {} saved with id {}", saved.getName(), saved.getId()),
                    () -> LOGGER.error("Error during {} save", item.getName()));
            return optLicensee;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during item save", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<WeaponJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.WEAPON.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during weapon get", e);
        }
        return Optional.empty();
    }

    @Override
    public List<WeaponJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during weapon get", e);

        }
        return new ArrayList<>();
    }

    @Override
    public Optional<WeaponJfxModel> update(final WeaponJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.WEAPON)
                    .set(Tables.WEAPON.NAME, item.getName())
                    .set(Tables.WEAPON.IDENTIFICATIONNUMBER, item.getIdentificationNumber())
                    .set(Tables.WEAPON.CALIBER, item.getCaliber())
                    .set(Tables.WEAPON.BUYDATE, item.getBuyDate())
                    .where(Tables.WEAPON.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            return this.getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during weapon update", e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(final WeaponJfxModel obj) {
        try {
            databaseConnectionService.getContext()
                    .delete(Tables.WEAPON)
                    .where(Tables.WEAPON.ID.eq(obj.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during weapon deletion", e);
        }
    }

    private Stream<WeaponJfxModel> internalGet(Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.WEAPON)
                .where(conditions)
                .fetch()
                .stream()
                .filter(record -> WeaponRecord.class.isAssignableFrom(record.getClass()))
                .map(WeaponRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private WeaponJfxModel convertToJfxModel(WeaponRecord weaponRecord) {
        WeaponJfxModelBuilder builder = new WeaponJfxModelBuilder()
                .setId(weaponRecord.getId())
                .setName(weaponRecord.getName())
                .setIdentificationNumber(weaponRecord.getIdentificationnumber())
                .setCaliber(weaponRecord.getCaliber())
                .setBuyDate(weaponRecord.getBuydate());

        return builder.createWeaponJfxModel();
    }
}
