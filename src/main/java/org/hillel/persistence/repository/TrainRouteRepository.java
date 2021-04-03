package org.hillel.persistence.repository;

import org.hibernate.Transaction;
import org.hillel.persistence.entity.TrainRoutesEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

@Repository
public class TrainRouteRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long create(final TrainRoutesEntity entity) {
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
