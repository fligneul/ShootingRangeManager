package com.fligneul.srm.dao.user;

import com.fligneul.srm.dao.IDAO;
import com.fligneul.srm.generated.jooq.Tables;
import com.fligneul.srm.generated.jooq.tables.records.UserRecord;
import com.fligneul.srm.service.DatabaseConnectionService;
import com.fligneul.srm.ui.model.user.ERole;
import com.fligneul.srm.ui.model.user.UserJfxModel;
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
 * DAO for user table
 */
public class UserDAO implements IDAO<UserJfxModel> {
    private static final Logger LOGGER = LogManager.getLogger(UserDAO.class);

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
     * Save a user and return the saved value
     *
     * @param item
     *         the model to save
     * @return the saved object, {@code Optional.empty()} if an error occurred
     */
    public Optional<UserJfxModel> save(@Nonnull final UserJfxModel item) {
        try {
            Optional<UserJfxModel> optFiringPost = Optional.ofNullable(databaseConnectionService.getContext()
                            .insertInto(Tables.USER)
                            .set(Tables.USER.USERNAME, item.getName())
                            .set(Tables.USER.ADMIN, ERole.ADMINISTRATOR.equals(item.getRole()))
                            .returning()
                            .fetchOne())
                    .map(this::convertToJfxModel);
            databaseConnectionService.getConnection().commit();

            optFiringPost.ifPresentOrElse(saved -> LOGGER.debug("User role {} saved with id {}", item.getName(), item),
                    () -> LOGGER.error("Error during user role save"));
            return optFiringPost;
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during user save", e);
        }
        return Optional.empty();
    }

    /**
     * Return a user by its id
     *
     * @param id
     *         the id of the desired item
     * @return the corresponding {@link UserJfxModel}, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<UserJfxModel> getById(final int id) {
        try {
            return internalGet(Tables.USER.ID.eq(id)).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during user get", e);
        }
        return Optional.empty();
    }

    /**
     * Return a user by its name
     *
     * @param name
     *         the name of the desired item
     * @return the corresponding {@link UserJfxModel}, {@code Optional.empty()} if an error occurred
     */
    public Optional<UserJfxModel> getByName(final String name) {
        try {
            return internalGet(Tables.USER.USERNAME.eq(name.toUpperCase())).findFirst();
        } catch (DataAccessException e) {
            LOGGER.error("Error during user get", e);
        }
        return Optional.empty();
    }

    /**
     * Return all user in DB
     *
     * @return a list of all {@link UserJfxModel}
     */
    @Override
    public List<UserJfxModel> getAll() {
        try {
            return internalGet().collect(Collectors.toList());
        } catch (DataAccessException e) {
            LOGGER.error("Error during user get", e);
        }
        return new ArrayList<>();
    }

    /**
     * Update a user and return the updated value
     *
     * @param item
     *         updated item to save
     * @return the updated object, {@code Optional.empty()} if an error occurred
     */
    @Override
    public Optional<UserJfxModel> update(@Nonnull final UserJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .update(Tables.USER)
                    .set(Tables.USER.USERNAME, item.getName())
                    .set(Tables.USER.ADMIN, ERole.ADMINISTRATOR.equals(item.getRole()))
                    .where(Tables.USER.ID.eq(item.getId()))
                    .execute();

            databaseConnectionService.getConnection().commit();
            return this.getById(item.getId());
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error user update", e);
        }
        return Optional.empty();
    }

    /**
     * Delete the provided user in DB
     *
     * @param item
     *         item to be deleted
     */
    @Override
    public void delete(@Nonnull final UserJfxModel item) {
        try {
            databaseConnectionService.getContext()
                    .delete(Tables.USER)
                    .where(Tables.USER.ID.eq(item.getId()))
                    .execute();
            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during user deletion", e);
        }
    }

    public void deleteByName(final String name) {
        try {
            databaseConnectionService.getContext()
                    .delete(Tables.USER)
                    .where(Tables.USER.USERNAME.eq(name))
                    .execute();
            databaseConnectionService.getConnection().commit();
        } catch (DataAccessException | SQLException e) {
            LOGGER.error("Error during user deletion", e);
        }
    }

    private Stream<UserJfxModel> internalGet(Condition... conditions) throws DataAccessException {
        return databaseConnectionService.getContext()
                .select()
                .from(Tables.USER)
                .where(conditions)
                .fetch()
                .stream()
                .filter(record -> UserRecord.class.isAssignableFrom(record.getClass()))
                .map(UserRecord.class::cast)
                .map(this::convertToJfxModel);
    }

    private UserJfxModel convertToJfxModel(UserRecord userRecord) {
        return new UserJfxModel(userRecord.getId(), userRecord.getUsername(), userRecord.getAdmin() ? ERole.ADMINISTRATOR : ERole.USER);
    }
}
