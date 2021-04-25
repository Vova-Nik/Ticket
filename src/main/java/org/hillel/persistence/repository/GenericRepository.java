package org.hillel.persistence.repository;

import org.hillel.exceptions.UnableToRemove;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<E, ID> {
    E createOrUpdate(E entity);

    Optional<E> findById(ID id);

    void removeById(ID id) throws UnableToRemove;

    void remove(E entity) throws UnableToRemove;

    Collection<E> findByIds(ID... ids);

    Optional<Collection<E>> findAll();

    Optional<Collection<E>> findAllSQL();

    Optional<Collection<E>> findAllCriteria();

    Optional<Collection<E>> storedProcExecute();

    boolean exists(ID id);

}
