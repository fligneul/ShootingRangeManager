package com.fligneul.srm.dao.status;

import com.fligneul.srm.dao.IDAO;
import com.fligneul.srm.jooq.Tables;
import com.fligneul.srm.jooq.tables.records.StatusRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.status.StatusJfxModel;
import com.fligneul.srm.ui.model.status.StatusJfxModelBuilder;
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

public class StatusDAO implements IDAO<StatusJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(StatusDAO.class);

    private DatabaseConnectionService databaseConnectionService;

    @Inject
    public void injectDependencies(final DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    @Override
    public Optional<StatusJfxModel> save(final StatusJfxModel item) {
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

    @Override
    public Optional<StatusJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.STATUS.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during status get", e);
        }
        return Optional.empty();
    }

    @Override
    public List<StatusJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during status get", e);

        }
        return new ArrayList<>();
    }

    @Override
    public Optional<StatusJfxModel> update(final StatusJfxModel item) {
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

    @Override
    public void delete(final StatusJfxModel obj) {
        try {
            databaseConnectionService.getContext()
                    .delete(Tables.STATUS)
                    .where(Tables.STATUS.ID.eq(obj.getId()))
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
                .where(conditions)
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
