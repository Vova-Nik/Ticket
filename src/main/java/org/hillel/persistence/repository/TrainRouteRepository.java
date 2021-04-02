package org.hillel.persistence.repository;

import org.hillel.persistence.entity.TrainRoutesEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TrainRouteRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(final TrainRoutesEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("StopEntity repository == null");
        }
//        if (!entity.isValid()) {
//            throw new IllegalArgumentException("StopEntity repository stopEntity is not valid");
//        }
        entityManager.persist(entity);
        return entity.getId();
    }
}
