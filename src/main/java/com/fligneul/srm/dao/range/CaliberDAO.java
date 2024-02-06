package com.fligneul.srm.dao.range;

import com.fligneul.srm.dao.IDAOWithForeignKey;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.CaliberRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.range.CaliberJfxModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Condition;
import org.jooq.exception.DataAccessException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DAO for caliber table
 */
public class CaliberDAO implements IDAOWithForeignKey<CaliberJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(CaliberDAO.class);

    private DatabaseConnectionService databaseConnectionService;

    /**
     * Inject GUICE dependencies
     *
     * @param databaseConnectionService
     *         connection service to the DB
     */
    @Inject
    public void injectDependencies(final DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    /**
     * Save a caliber and return the saved value
     *
     * @param item
     *         the model to save
     * @return the saved object, {@code Optional.empty()} if an error occurred
     */
    public Optional<CaliberJfxModel> save(final int foreignId, @Nonnull final CaliberJfxModel item) {
        try {
            Optional<CaliberJfxModel> optCaliber = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.CALIBER)
                            .set(Tables.CALIBER.FIRINGPOINTID, foreignId)
                            .set(Tables.CALIBER.NAME, item.getName())
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            optCaliber.ifPresentOrElse(saved -> LOGGER.debug("Caliber {} saved with id {}", saved.getName(), saved.getId()),
                    () -> LOGGER.error("Error during {} save", item.getName()));
            return optCaliber;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during caliber save", e);
        }
        return Optional.empty();
    }

    /**
     * Return a caliber by its id
     *
     * @param id
     *         the id of the desired item
     * @return the corresponding {@link CaliberJfxModel}, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<CaliberJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.CALIBER.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during caliber get", e);
        }
        return Optional.empty();
    }

    /**
     * Return all calibers in DB
     *
     * @return a list of all {@link CaliberJfxModel}
     */
    @Override
    public List<CaliberJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during caliber get", e);

        }
        return new ArrayList<>();
    }

    /**
     * Return all calibers associated with the provided firing point in DB
     *
     * @param pointId
     *         the firing point id
     * @return a list of all {@link CaliberJfxModel} associated with the provided firing point
     */
    public List<CaliberJfxModel> getAllByPointId(int pointId) {
        try {
            return internalGet(Tables.CALIBER.FIRINGPOINTID.eq(pointId))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during caliber get", e);

        }
        return new ArrayList<>();
    }

    /**
     * Update a caliber and return the updated value
     *
     * @param item
     *         updated item to save
     * @return the updated object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<CaliberJfxModel> update(@Nonnull final CaliberJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.CALIBER)
                    .set(Tables.CALIBER.NAME, item.getName())
                    .where(Tables.CALIBER.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            return this.getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error caliber update", e);
        }
        return Optional.empty();
    }

    /**
     * Delete the provided caliber in DB
     *
     * @param item
     *         item to be deleted
     */
    @Override
    public void delete(@Nonnull final CaliberJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.CALIBER)
                    .set(Tables.CALIBER.DELETED, true)
                    .where(Tables.CALIBER.ID.eq(item.getId()))
                    .execute();
            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during caliber deletion", e);
        }
    }

    private Stream<CaliberJfxModel> internalGet(Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.CALIBER)
                .where(Stream.concat(Stream.of(Tables.CALIBER.DELETED.eq(false)), Stream.of(conditions)).toArray(Condition[]::new))
                .fetch()
                .stream()
                .filter(record -> CaliberRecord.class.isAssignableFrom(record.getClass()))
                .map(CaliberRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private CaliberJfxModel convertToJfxModel(CaliberRecord caliberRecord) {
        return new CaliberJfxModel(caliberRecord.getId(), caliberRecord.getName());
    }
}
