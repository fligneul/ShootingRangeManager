package com.fligneul.srm.dao.range;

import com.fligneul.srm.dao.IDAO;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.FiringpointRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.range.FiringPointJfxModel;
import javafx.collections.FXCollections;
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
 * DAO for firing point table
 */
public class FiringPointDAO implements IDAO<FiringPointJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(FiringPointDAO.class);

    private DatabaseConnectionService databaseConnectionService;
    private FiringPostDAO firingPostDAO;

    /**
     * Inject GUICE dependencies
     *
     * @param databaseConnectionService
     *         connection service to the DB
     * @param firingPostDAO
     *         DAO for firing post table
     */
    @Inject
    public void injectDependencies(final DatabaseConnectionService databaseConnectionService,
                                   final FiringPostDAO firingPostDAO) {
        this.databaseConnectionService = databaseConnectionService;
        this.firingPostDAO = firingPostDAO;
    }

    /**
     * Save a firing point and return the saved value
     *
     * @param item
     *         the model to save
     * @return the saved object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<FiringPointJfxModel> save(@Nonnull final FiringPointJfxModel item) {
        try {
            Optional<FiringPointJfxModel> optFiringPoint = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.FIRINGPOINT)
                            .set(Tables.FIRINGPOINT.NAME, item.getName())
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            optFiringPoint.ifPresentOrElse(saved -> LOGGER.debug("Firing post {} saved with id {}", saved.getName(), saved.getId()),
                    () -> LOGGER.error("Error during {} save", item.getName()));
            return optFiringPoint;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during firingPoint save", e);
        }
        return Optional.empty();
    }

    /**
     * Return a firing point by its id
     *
     * @param id
     *         the id of the desired item
     * @return the corresponding {@link FiringPointJfxModel}, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<FiringPointJfxModel> getById(int id) {
        try {
            return internalGet(Tables.FIRINGPOINT.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during firingPoint get", e);
        }
        return Optional.empty();
    }

    /**
     * Return all firing point in DB
     *
     * @return a list of all {@link FiringPointJfxModel}
     */
    @Override
    public List<FiringPointJfxModel> getAll() {
        try {
            return internalGet()
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during firingPoint get", e);

        }
        return new ArrayList<>();
    }


    /**
     * Update a firing point and return the updated value
     *
     * @param item
     *         updated item to save
     * @return the updated object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<FiringPointJfxModel> update(@Nonnull final FiringPointJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.FIRINGPOINT)
                    .set(Tables.FIRINGPOINT.NAME, item.getName())
                    .where(Tables.FIRINGPOINT.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            item.getPosts().forEach(firingPostDAO::update);
            return this.getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error firing point update", e);
        }
        return Optional.empty();
    }

    /**
     * Delete the provided firing point in DB
     *
     * @param item
     *         item to be deleted
     */
    @Override
    public void delete(@Nonnull final FiringPointJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .delete(Tables.FIRINGPOINT)
                    .where(Tables.FIRINGPOINT.ID.eq(item.getId()))
                    .execute();
            databaseConnectionService.getConnection().commit();

        } catch (SQLException e) {
            LOGGER.error("Error during firingPoint deletion", e);
        }
    }

    private Stream<FiringPointJfxModel> internalGet(Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.FIRINGPOINT)
                .where(conditions)
                .fetch()
                .stream()
                .filter(record -> FiringpointRecord.class.isAssignableFrom(record.getClass()))
                .map(FiringpointRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private FiringPointJfxModel convertToJfxModel(FiringpointRecord firingPointRecord) {
        return new FiringPointJfxModel(firingPointRecord.getId(), firingPointRecord.getName(), FXCollections.observableList(firingPostDAO.getAllByPointId(firingPointRecord.getId())));
    }
}
