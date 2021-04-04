package org.hillel.persistence.repository;

import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.entity.VehicleEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class VehicleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Long save(final VehicleEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("VehicleRepository.create VehicleEntity == null");
        }
        if (!entity.isValid()) {
            throw new IllegalArgumentException("VehicleRepository.create VehicleEntity not valid data");
        }
        entityManager.persist(entity);
        return entity.getId();
    }

    public Optional<VehicleEntity> getByName(String name) {
        String sql = "SELECT * FROM vehicles where name = ?";
        Query query = entityManager.createNativeQuery(sql, VehicleEntity.class);
        query.setParameter(1, name);
        VehicleEntity vehicleEntity = (VehicleEntity) query.getSingleResult();
        return Optional.ofNullable(vehicleEntity);
    }
}
