package org.hillel.persistence.repository;

import org.hillel.persistence.entity.VehicleEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class VehicleRepository extends ComonRepository<VehicleEntity, Long> {
    VehicleRepository(){
        super(VehicleEntity.class);
    }

    public Optional<VehicleEntity> getByName(String name) {
        String sql = "SELECT * FROM vehicles where name = ?";
        Query query = entityManager.createNativeQuery(sql, VehicleEntity.class);
        query.setParameter(1, name);
        VehicleEntity vehicleEntity = (VehicleEntity) query.getSingleResult();
        return Optional.ofNullable(vehicleEntity);
    }
}
