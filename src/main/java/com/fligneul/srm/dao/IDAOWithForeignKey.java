package com.fligneul.srm.dao;

import java.util.Optional;

/**
 * DAO interface for DB persistence of JFX models with a foreign key
 *
 * @param <T>
 *         the JFX model to be persisted
 */
public interface IDAOWithForeignKey<T> extends IDAO<T> {

    /**
     * Should not use this implementation
     * See {@link #save(int, Object)} instead
     *
     * @param item
     *         the model to save
     * @return an Optional of the saved item if successful, empty otherwise
     */
    default Optional<T> save(final T item) {
        // Should use the save with the foreign key
        return Optional.empty();
    }

    /**
     * Save a JFX model to DB
     *
     * @param item
     *         the model to save
     * @return an Optional of the saved item if successful, empty otherwise
     */
    Optional<T> save(int foreignId, final T item);
}
