package org.hillel.persistence.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<E, ID> {
    E createOrUpdate(E entity);

    Optional<E> findById(ID id);

    void removeById(ID id);

    void remove(E entity);

    //    List<E> findByName(String name);
}
