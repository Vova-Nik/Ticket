package org.hillel.persistence.repository;

import org.hillel.exceptions.UnableToRemove;

import java.util.Collection;
import java.util.Optional;

public interface GenericRepository<E, ID> {
    E createOrUpdate(E entity);

    Optional<E> findById(ID id);

    Collection<E> findByNameActive(String name);

    Collection<E> findByName(String name);

    void removeById(ID id) throws UnableToRemove;

    void remove(E entity) throws UnableToRemove;

    Collection<E> findByIds(ID... ids);

    Collection<E> findAll();

    Collection<E> findAllSQL();

    Collection<E> findAllCriteria();

    Collection<E> storedProcExecute();

    boolean exists(ID id);

}
