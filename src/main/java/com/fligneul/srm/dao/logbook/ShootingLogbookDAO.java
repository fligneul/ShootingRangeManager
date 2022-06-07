package com.fligneul.srm.dao.logbook;

import com.fligneul.srm.dao.IDAO;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.ShootinglogbookRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.logbook.ShootingLogbookJfxModel;
import javafx.collections.FXCollections;
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

public class ShootingLogbookDAO implements IDAO<ShootingLogbookJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(ShootingLogbookDAO.class);

    private DatabaseConnectionService databaseConnectionService;
    private ShootingSessionDAO shootingSessionDAO;

    @Inject
    public void injectDependencies(final DatabaseConnectionService databaseConnectionService,
                                   final ShootingSessionDAO shootingSessionDAO) {
        this.databaseConnectionService = databaseConnectionService;
        this.shootingSessionDAO = shootingSessionDAO;
    }

    @Override
    public Optional<ShootingLogbookJfxModel> save(final ShootingLogbookJfxModel item) {
        try {
            Optional<ShootingLogbookJfxModel> optFiringPoint = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.SHOOTINGLOGBOOK)
                            .set(Tables.SHOOTINGLOGBOOK.CREATIONDATE, item.getCreationDate())
                            .set(Tables.SHOOTINGLOGBOOK.KNOWLEDGECHECKDATE, item.getKnowledgeCheckDate())
                            .set(Tables.SHOOTINGLOGBOOK.WHITETARGETLEVEL, item.hasWhiteTargetLevel())
                            .set(Tables.SHOOTINGLOGBOOK.LICENSEEID, item.getLicenseeId())
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            optFiringPoint.ifPresentOrElse(saved -> LOGGER.debug("Shooting logbook {} saved with id {}", saved.getCreationDate(), saved.getId()),
                    () -> LOGGER.error("Error during {} save", item.getCreationDate()));
            return optFiringPoint;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during shooting logbook save", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ShootingLogbookJfxModel> getById(int id) {
        try {
            return internalGet(Tables.SHOOTINGLOGBOOK.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during shooting logbook get", e);
        }
        return Optional.empty();
    }

    public Optional<ShootingLogbookJfxModel> getByLicenseeId(int id) {
        try {
            return internalGet(Tables.SHOOTINGLOGBOOK.LICENSEEID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during shooting logbook get", e);
        }
        return Optional.empty();
    }

    @Override
    public List<ShootingLogbookJfxModel> getAll() {
        try {
            return internalGet()
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during shooting logbook get", e);

        }
        return new ArrayList<>();
    }

    @Override
    public Optional<ShootingLogbookJfxModel> update(final ShootingLogbookJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.SHOOTINGLOGBOOK)
                    .set(Tables.SHOOTINGLOGBOOK.CREATIONDATE, item.getCreationDate())
                    .set(Tables.SHOOTINGLOGBOOK.KNOWLEDGECHECKDATE, item.getKnowledgeCheckDate())
                    .set(Tables.SHOOTINGLOGBOOK.WHITETARGETLEVEL, item.hasWhiteTargetLevel())
                    .where(Tables.SHOOTINGLOGBOOK.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            item.getSessions().forEach(shootingSessionDAO::update);
            return this.getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error shooting logbook update", e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(final ShootingLogbookJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .delete(Tables.SHOOTINGLOGBOOK)
                    .where(Tables.SHOOTINGLOGBOOK.ID.eq(item.getId()))
                    .execute();
            databaseConnectionService.getConnection().commit();

        } catch (SQLException e) {
            LOGGER.error("Error during shooting logbook deletion", e);
        }
    }

    private Stream<ShootingLogbookJfxModel> internalGet(Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.SHOOTINGLOGBOOK)
                .where(conditions)
                .fetch()
                .stream()
                .filter(record -> ShootinglogbookRecord.class.isAssignableFrom(record.getClass()))
                .map(ShootinglogbookRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private ShootingLogbookJfxModel convertToJfxModel(ShootinglogbookRecord shootingLogbookRecord) {
        final ShootingLogbookJfxModel shootingLogbookJfxModel = new ShootingLogbookJfxModel(shootingLogbookRecord.getId(),
                shootingLogbookRecord.getCreationdate(),
                FXCollections.observableList(shootingSessionDAO.getAllByLogbookId(shootingLogbookRecord.getId())),
                shootingLogbookRecord.getLicenseeid());

        Optional.ofNullable(shootingLogbookRecord.getKnowledgecheckdate()).ifPresent(shootingLogbookJfxModel::setKnowledgeCheckDate);
        shootingLogbookJfxModel.setWhiteTargetLevel(shootingLogbookRecord.getWhitetargetlevel());
        return shootingLogbookJfxModel;
    }
}
