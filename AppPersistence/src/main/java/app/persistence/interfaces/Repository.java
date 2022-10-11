package app.persistence.interfaces;

import app.model.Entity;

import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {
    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    E findOne(ID id);

    /**
     * @return all entities
     */
    List<E> findAll();

    /**
     * @param entity entity must be not null
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    E save(E entity);

}
