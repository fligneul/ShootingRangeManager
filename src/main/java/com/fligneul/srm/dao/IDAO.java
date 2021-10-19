package com.fligneul.srm.dao;

import java.util.List;
import java.util.Optional;

/**
 * DAO interface for DB persistence of JFX models
 *
 * @param <T>
 *         the JFX model to be persisted
 */
public interface IDAO<T> {

    /**
     * Save a JFX model to DB
     *
     * @param item
     *         the model to save
     * @return an Optional of the saved item if successful, empty otherwise
     */
    Optional<T> save(final T item);

    /**
     * Return the JFX model of a saved data by its id
     *
     * @param id
     *         the id of the desired item
     * @return an Optional of the searched item if successful, empty otherwise
     */
    Optional<T> getById(final int id);

    /**
     * Return the JFX model of all saved data
     *
     * @return the list of all items if successful, empty otherwise
     */
    List<T> getAll();

    /**
     * Update the saved JFX model with the provided item
     *
     * @param item
     *         updated item to save
     * @return an Optional of the updated item if successful, empty otherwise
     */
    Optional<T> update(final T item);

    /**
     * Delete the provided item from the DB
     *
     * @param item
     *         item to be deleted
     */
    void delete(final T item);

}
