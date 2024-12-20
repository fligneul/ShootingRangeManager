package com.fligneul.srm.dao.status;

import com.fligneul.srm.dao.IDAO;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.StatusRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModelBuilder;
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
 * DAO for status table
 */
public class StatusDAO implements IDAO<StatusJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(StatusDAO.class);

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
     * Save a licensee status and return the saved value
     *
     * @param item
     *         the model to save
     * @return the saved object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<StatusJfxModel> save(@Nonnull final StatusJfxModel item) {
        try {
            Optional<StatusJfxModel> optLicensee = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.STATUS)
                            .set(Tables.STATUS.NAME, item.getName())
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            optLicensee.ifPresentOrElse(saved -> LOGGER.debug("Status {} saved with id {}", saved.getName(), saved.getId()),
                    () -> LOGGER.error("Error during {} save", item.getName()));
            return optLicensee;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during item save", e);
        }
        return Optional.empty();
    }

    /**
     * Return a licensee status by its id
     *
     * @param id
     *         the id of the desired item
     * @return the corresponding {@link StatusJfxModel}, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<StatusJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.STATUS.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during status get", e);
        }
        return Optional.empty();
    }

    /**
     * Return all licensee status in DB
     *
     * @return a list of all {@link StatusJfxModel}
     */
    @Override
    public List<StatusJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during status get", e);

        }
        return new ArrayList<>();
    }

    /**
     * Update a licensee status and return the updated value
     *
     * @param item
     *         updated item to save
     * @return the updated object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<StatusJfxModel> update(@Nonnull final StatusJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.STATUS)
                    .set(Tables.STATUS.NAME, item.getName())
                    .where(Tables.STATUS.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            return this.getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during status update", e);
        }
        return Optional.empty();
    }

    /**
     * Delete the provided licensee status in DB
     *
     * @param item
     *         item to be deleted
     */
    @Override
    public void delete(@Nonnull final StatusJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.STATUS)
                    .set(Tables.STATUS.DELETED, true)
                    .where(Tables.STATUS.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during status deletion", e);
        }
    }

    private Stream<StatusJfxModel> internalGet(Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.STATUS)
                .where(Stream.concat(Stream.of(Tables.STATUS.DELETED.eq(false)), Stream.of(conditions)).toArray(Condition[]::new))
                .fetch()
                .stream()
                .filter(record -> StatusRecord.class.isAssignableFrom(record.getClass()))
                .map(StatusRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private StatusJfxModel convertToJfxModel(StatusRecord statusRecord) {
        StatusJfxModelBuilder builder = new StatusJfxModelBuilder()
                .setId(statusRecord.getId())
                .setName(statusRecord.getName());

        return builder.createStatusJfxModel();
    }
}
