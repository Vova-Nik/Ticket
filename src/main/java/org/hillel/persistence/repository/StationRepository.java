package org.hillel.persistence.repository;

import org.hillel.persistence.entity.ClientEntity;
import org.hillel.persistence.entity.StationEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class StationRepository extends ComonRepository<StationEntity, Long>{
    protected StationRepository() {
        super(StationEntity.class);
    }

    public StationEntity getByName(String name) {
        String sql = "SELECT * FROM stations where name = ?";
        Query query = entityManager.createNativeQuery(sql,StationEntity.class);
        query.setParameter(1, name);
        StationEntity stationEntity = (StationEntity) query.getSingleResult();
        return stationEntity;
    }
}
