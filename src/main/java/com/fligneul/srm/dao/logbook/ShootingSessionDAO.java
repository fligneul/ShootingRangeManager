package com.fligneul.srm.dao.logbook;

import com.fligneul.srm.dao.IDAOWithForeignKey;
import com.fligneul.srm.dao.weapon.WeaponDAO;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.ShootingsessionRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.logbook.ShootingSessionJfxModel;
import com.fligneul.srm.ui.model.weapon.WeaponJfxModel;
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

public class ShootingSessionDAO implements IDAOWithForeignKey<ShootingSessionJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(ShootingSessionDAO.class);

    private DatabaseConnectionService databaseConnectionService;
    private WeaponDAO weaponDAO;

    @Inject
    public void injectDependencies(final DatabaseConnectionService databaseConnectionService,
                                   final WeaponDAO weaponDAO) {
        this.databaseConnectionService = databaseConnectionService;
        this.weaponDAO = weaponDAO;
    }

    public Optional<ShootingSessionJfxModel> save(final int foreignId, final ShootingSessionJfxModel item) {
        try {
            Optional<ShootingSessionJfxModel> optFiringPost = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.SHOOTINGSESSION)
                            .set(Tables.SHOOTINGSESSION.SHOOTINGLOGBOOKID, foreignId)
                            .set(Tables.SHOOTINGSESSION.SESSIONDATE, item.getSessionDate())
                            .set(Tables.SHOOTINGSESSION.INSTRUCTORNAME, item.getInstructorName())
                            .set(Tables.SHOOTINGSESSION.WEAPONID, item.getWeapon().map(WeaponJfxModel::getId).orElse(null))
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            optFiringPost.ifPresentOrElse(saved -> LOGGER.debug("Shooting session {} saved with id {}", saved.getSessionDate(), saved.getId()),
                    () -> LOGGER.error("Error during {} save", item.getSessionDate()));
            return optFiringPost;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during shooting session save", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ShootingSessionJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.SHOOTINGSESSION.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during shooting session get", e);
        }
        return Optional.empty();
    }

    @Override
    public List<ShootingSessionJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during shooting session get", e);

        }
        return new ArrayList<>();
    }

    public List<ShootingSessionJfxModel> getAllByLogbookId(int logbookId) {
        try {
            return internalGet(Tables.SHOOTINGSESSION.SHOOTINGLOGBOOKID.eq(logbookId))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during shooting session get", e);

        }
        return new ArrayList<>();
    }

    @Override
    public Optional<ShootingSessionJfxModel> update(final ShootingSessionJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.SHOOTINGSESSION)
                    .set(Tables.SHOOTINGSESSION.SESSIONDATE, item.getSessionDate())
                    .set(Tables.SHOOTINGSESSION.INSTRUCTORNAME, item.getInstructorName())
                    .set(Tables.SHOOTINGSESSION.WEAPONID, item.getWeapon().map(WeaponJfxModel::getId).orElse(null))
                    .where(Tables.SHOOTINGSESSION.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            return this.getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error shooting session update", e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(final ShootingSessionJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .delete(Tables.SHOOTINGSESSION)
                    .where(Tables.SHOOTINGSESSION.ID.eq(item.getId()))
                    .execute();
            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during shooting session deletion", e);
        }
    }

    private Stream<ShootingSessionJfxModel> internalGet(Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.SHOOTINGSESSION)
                .where(conditions)
                .fetch()
                .stream()
                .filter(record -> ShootingsessionRecord.class.isAssignableFrom(record.getClass()))
                .map(ShootingsessionRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private ShootingSessionJfxModel convertToJfxModel(ShootingsessionRecord shootingSessionRecord) {
        ShootingSessionJfxModel shootingSessionJfxModel = new ShootingSessionJfxModel(shootingSessionRecord.getId(),
                shootingSessionRecord.getSessiondate(), shootingSessionRecord.getInstructorname());

        Optional.ofNullable(shootingSessionRecord.getWeaponid()).ifPresent(weaponId -> shootingSessionJfxModel.setWeapon(weaponDAO.getById(weaponId).orElseThrow()));

        return shootingSessionJfxModel;
    }
}
