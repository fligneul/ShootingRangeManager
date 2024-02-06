package com.fligneul.srm.dao.range;

import com.fligneul.srm.dao.IDAOWithForeignKey;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.TargetholderRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.range.TargetHolderJfxModel;
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
 * DAO for target holder table
 */
public class TargetHolderDAO implements IDAOWithForeignKey<TargetHolderJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(TargetHolderDAO.class);

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
     * Save a target holder and return the saved value
     *
     * @param item
     *         the model to save
     * @return the saved object, {@code Optional.empty()} if an error occurred
     */
    public Optional<TargetHolderJfxModel> save(final int foreignId, @Nonnull final TargetHolderJfxModel item) {
        try {
            Optional<TargetHolderJfxModel> optTargetSupport = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.TARGETHOLDER)
                            .set(Tables.TARGETHOLDER.FIRINGPOINTID, foreignId)
                            .set(Tables.TARGETHOLDER.NAME, item.getName())
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            optTargetSupport.ifPresentOrElse(saved -> LOGGER.debug("Target holder {} saved with id {}", saved.getName(), saved.getId()),
                    () -> LOGGER.error("Error during {} save", item.getName()));
            return optTargetSupport;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during target holder save", e);
        }
        return Optional.empty();
    }

    /**
     * Return a target holder by its id
     *
     * @param id
     *         the id of the desired item
     * @return the corresponding {@link TargetHolderJfxModel}, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<TargetHolderJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.TARGETHOLDER.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during target holder get", e);
        }
        return Optional.empty();
    }

    /**
     * Return all target holders in DB
     *
     * @return a list of all {@link TargetHolderJfxModel}
     */
    @Override
    public List<TargetHolderJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during target holder get", e);

        }
        return new ArrayList<>();
    }

    /**
     * Return all target holders associated with the provided firing point in DB
     *
     * @param pointId
     *         the firing point id
     * @return a list of all {@link TargetHolderJfxModel} associated with the provided firing point
     */
    public List<TargetHolderJfxModel> getAllByPointId(int pointId) {
        try {
            return internalGet(Tables.TARGETHOLDER.FIRINGPOINTID.eq(pointId))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during target holder get", e);

        }
        return new ArrayList<>();
    }

    /**
     * Update a target holder and return the updated value
     *
     * @param item
     *         updated item to save
     * @return the updated object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<TargetHolderJfxModel> update(@Nonnull final TargetHolderJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.TARGETHOLDER)
                    .set(Tables.TARGETHOLDER.NAME, item.getName())
                    .where(Tables.TARGETHOLDER.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            return this.getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error target support update", e);
        }
        return Optional.empty();
    }

    /**
     * Delete the provided target holder in DB
     *
     * @param item
     *         item to be deleted
     */
    @Override
    public void delete(@Nonnull final TargetHolderJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.TARGETHOLDER)
                    .set(Tables.TARGETHOLDER.DELETED, true)
                    .where(Tables.TARGETHOLDER.ID.eq(item.getId()))
                    .execute();
            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during target holder deletion", e);
        }
    }

    private Stream<TargetHolderJfxModel> internalGet(Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.TARGETHOLDER)
                .where(Stream.concat(Stream.of(Tables.TARGETHOLDER.DELETED.eq(false)), Stream.of(conditions)).toArray(Condition[]::new))
                .fetch()
                .stream()
                .filter(record -> TargetholderRecord.class.isAssignableFrom(record.getClass()))
                .map(TargetholderRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private TargetHolderJfxModel convertToJfxModel(TargetholderRecord targetholderRecord) {
        return new TargetHolderJfxModel(targetholderRecord.getId(), targetholderRecord.getName());
    }
}
