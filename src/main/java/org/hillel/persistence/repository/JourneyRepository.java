package org.hillel.persistence.repository;

import org.hillel.persistence.entity.JourneyEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class JourneyRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(final JourneyEntity journeyEntity){
//        if(journeyEntity == null) {
//            throw new IllegalArgumentException("JourneyEntity == null");
//        }
//        if(!journeyEntity.isValid()){
//            throw new IllegalArgumentException("JourneyEntity == null");
//        }
        entityManager.persist(journeyEntity);
        return journeyEntity.getId();
    }
}
