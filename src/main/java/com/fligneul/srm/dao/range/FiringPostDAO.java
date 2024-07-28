package com.fligneul.srm.dao.range;

import com.fligneul.srm.dao.IDAOWithForeignKey;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.FiringpostRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.range.FiringPostJfxModel;
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
 * DAO for firing post table
 */
public class FiringPostDAO implements IDAOWithForeignKey<FiringPostJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(FiringPostDAO.class);

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
     * Save a firing post and return the saved value
     *
     * @param item
     *         the model to save
     * @return the saved object, {@code Optional.empty()} if an error occurred
     */
    public Optional<FiringPostJfxModel> save(final int foreignId, @Nonnull final FiringPostJfxModel item) {
        try {
            Optional<FiringPostJfxModel> optFiringPost = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.FIRINGPOST)
                            .set(Tables.FIRINGPOST.FIRINGPOINTID, foreignId)
                            .set(Tables.FIRINGPOST.NAME, item.getName())
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            optFiringPost.ifPresentOrElse(saved -> LOGGER.debug("Firing post {} saved with id {}", saved.getName(), saved.getId()),
                    () -> LOGGER.error("Error during {} save", item.getName()));
            return optFiringPost;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during firingPost save", e);
        }
        return Optional.empty();
    }

    /**
     * Return a firing post by its id
     *
     * @param id
     *         the id of the desired item
     * @return the corresponding {@link FiringPostJfxModel}, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<FiringPostJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.FIRINGPOST.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during firingPost get", e);
        }
        return Optional.empty();
    }

    /**
     * Return all firing post in DB
     *
     * @return a list of all {@link FiringPostJfxModel}
     */
    @Override
    public List<FiringPostJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during firingPost get", e);

        }
        return new ArrayList<>();
    }

    /**
     * Return all firing post associated with the provided firing point in DB
     *
     * @param pointId
     *         the firing point id
     * @return a list of all {@link FiringPostJfxModel} associated with the provided firing point
     */
    public List<FiringPostJfxModel> getAllByPointId(int pointId) {
        try {
            return internalGet(Tables.FIRINGPOST.FIRINGPOINTID.eq(pointId))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during firingPost get", e);

        }
        return new ArrayList<>();
    }

    /**
     * Update a firing post and return the updated value
     *
     * @param item
     *         updated item to save
     * @return the updated object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<FiringPostJfxModel> update(@Nonnull final FiringPostJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.FIRINGPOST)
                    .set(Tables.FIRINGPOST.NAME, item.getName())
                    .where(Tables.FIRINGPOST.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            return this.getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error firing post update", e);
        }
        return Optional.empty();
    }

    /**
     * Delete the provided firing post in DB
     *
     * @param item
     *         item to be deleted
     */
    @Override
    public void delete(@Nonnull final FiringPostJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.FIRINGPOST)
                    .set(Tables.FIRINGPOST.DELETED, true)
                    .where(Tables.FIRINGPOST.ID.eq(item.getId()))
                    .execute();
            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during firing post deletion", e);
        }
    }

    private Stream<FiringPostJfxModel> internalGet(Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.FIRINGPOST)
                .where(Stream.concat(Stream.of(Tables.FIRINGPOST.DELETED.eq(false)), Stream.of(conditions)).toArray(Condition[]::new))
                .fetch()
                .stream()
                .filter(record -> FiringpostRecord.class.isAssignableFrom(record.getClass()))
                .map(FiringpostRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private FiringPostJfxModel convertToJfxModel(FiringpostRecord firingPostRecord) {
        return new FiringPostJfxModel(firingPostRecord.getId(), firingPostRecord.getName());
    }
}
