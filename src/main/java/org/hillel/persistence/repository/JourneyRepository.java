package org.hillel.persistence.repository;

import org.hillel.persistence.entity.ClientEntity;
import org.hillel.persistence.entity.JourneyEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class JourneyRepository extends ComonRepository<JourneyEntity, Long>{
    protected JourneyRepository() {
        super(JourneyEntity.class);
    }

}
