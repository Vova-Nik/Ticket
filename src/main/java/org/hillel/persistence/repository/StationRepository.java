package org.hillel.persistence.repository;

import org.hillel.persistence.entity.StationEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class StationRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public long create(final StationEntity stationEntity) {
        if (stationEntity == null) {
            throw new IllegalArgumentException("StationRepository repository == null");
        }
        if (!stationEntity.isValid()) {
            throw new IllegalArgumentException("StationRepository repository stopEntity is not valid");
        }
        entityManager.persist(stationEntity);
        return stationEntity.getId();
    }

    public StationEntity getByName(String name) {
        String sql = "SELECT * FROM stations where name = ?";
        Query query = entityManager.createNativeQuery(sql,StationEntity.class);
        query.setParameter(1, name);
        StationEntity stationEntity = (StationEntity) query.getSingleResult();
        return stationEntity;
    }
}
