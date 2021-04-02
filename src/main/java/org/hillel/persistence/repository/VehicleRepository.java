package org.hillel.persistence.repository;

import org.hillel.persistence.entity.VehicleEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
@Repository
public class VehicleRepository {
        @PersistenceContext
        private EntityManager entityManager;

        public Long save(final VehicleEntity entity){
            if(entity == null) {
                throw new IllegalArgumentException("VehicleRepository.create VehicleEntity == null");
            }
            if(!entity.isValid()){
                throw new IllegalArgumentException("VehicleRepository.create VehicleEntity not valid data");
            }
            entityManager.persist(entity);
            return entity.getId();
        }
    }
