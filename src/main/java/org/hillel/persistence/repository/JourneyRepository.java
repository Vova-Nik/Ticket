package org.hillel.persistence.repository;

import org.hillel.persistence.entity.ClientEntity;
import org.hillel.persistence.entity.JourneyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class JourneyRepository extends ComonRepository<JourneyEntity, Long>{
    @PersistenceContext
    protected EntityManager entityManager;
    protected JourneyRepository() {
        super(JourneyEntity.class);
    }
}
