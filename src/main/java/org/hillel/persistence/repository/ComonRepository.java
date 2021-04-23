package org.hillel.persistence.repository;

import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.AbstractEntity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.*;

public abstract class ComonRepository<E extends AbstractEntity<ID>, ID extends Serializable> implements GenericRepository<E, ID> {

    private final Class<E> entityClass;
    @PersistenceContext
    protected EntityManager entityManager;

    protected ComonRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public E createOrUpdate(E entity) {
        if (entity == null || !entity.isValid())
            throw new IllegalArgumentException("Repository.createOrUpdate not valid entity");
        if (Objects.isNull(entity.getId())) {
            entityManager.persist(entity);
            return entity;
        }
        return entityManager.merge(entity);
    }

    @Override
    public Optional<E> findById(ID id) {
        if (Objects.isNull(id)) throw new IllegalArgumentException("Repository.createOrUpdate not valid entity");
        E entity = entityManager.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public void removeById(ID id) throws UnableToRemove {
        if (id == null)
            throw new IllegalArgumentException("Repository.delete unsufficient entity");
        entityManager.remove(entityManager.getReference(entityClass, id));
    }

    @Override
    public void remove(E entity) throws UnableToRemove {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("Repository.delete insufficient id");
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        }else {
            removeById(entity.getId());
        }
    }

    @Override
    public Collection<E> findByIds(ID... ids) {
        List<E> list = new ArrayList<>();
        for (ID id : ids
        ) {
            if (Objects.isNull(id)) throw new IllegalArgumentException("Not valid ID in repository findByIds");
            list.add(findById(id).orElseThrow(() -> new IllegalArgumentException("Not valid ID in repository findByIds")));
        }
        return list;
    }

    @Override
    public Collection<E> findAll() {
        return null;
    }

    @Override
    public boolean exists(ID id) {
        Optional<E> entity = findById(id);
        return entity.isPresent();
    }
}


