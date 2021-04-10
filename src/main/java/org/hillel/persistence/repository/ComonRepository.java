package org.hillel.persistence.repository;

import org.hillel.persistence.entity.AbstractEntity;
import org.hillel.persistence.entity.StationEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

//    @Override
//    public List<E> findByName(String name) {
//        List<E> entities = new ArrayList<>();
//        String sql = "SELECT * FROM stations where name = ?";
//        Query query = entityManager.createNativeQuery(sql, entityClass);
//        query.setParameter(1, name);
        //  StationEntity stationEntity = (StationEntity) query.getSingleResult();
        //  entities =(ArrayList<E>) query.getResultList();
//        return (ArrayList<E>)query.getResultList();
//    }

    @Override
    public void removeById(ID id) {
        throw new UnsupportedOperationException("removeById not implemented yet");
    }

    @Override
    public void remove(E entity) {
        throw new UnsupportedOperationException("removeById not implemented yet");
    }


}


