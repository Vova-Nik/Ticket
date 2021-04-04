package org.hillel.persistence.repository;

import org.hillel.persistence.entity.RouteEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RouteRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long create(final RouteEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("StopEntity repository == null");
        }
        if (!entity.isValid()) {
            throw new IllegalArgumentException("StopEntity repository stopEntity is not valid");
        }
//        EntityTransaction tr = entityManager.getTransaction();//.begin();
//        tr.begin();
        entityManager.persist(entity);
//        tr.commit();
        return entity.getId();
    }
}
