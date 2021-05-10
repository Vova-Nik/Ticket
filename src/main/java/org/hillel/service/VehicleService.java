package org.hillel.service;

import org.hillel.persistence.entity.TripEntity_;
import org.hillel.persistence.entity.VehicleEntity;
import org.hillel.persistence.entity.VehicleEntity_;
import org.hillel.persistence.jpa.repository.VehicleJPARepository;
import org.hillel.persistence.jpa.repository.specification.VehicleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service(value = "vehicleService")

public class VehicleService extends EntityServiceImplementation<VehicleEntity, Long>{

    private final VehicleJPARepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleJPARepository vehicleRepository){
        super(VehicleEntity.class, vehicleRepository);
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    boolean isValid(VehicleEntity vehicle) {
        return vehicle.isValid();
    }

    @Transactional
    public List<VehicleEntity> getByNameActiveSpecification(final String name) {
        if (Objects.isNull(name))
            throw new IllegalArgumentException("VehicleEntity.getByNameActiveSpecification insufficient name parameter");
        return vehicleRepository.findAll(VehicleSpecification.byName(name).and(VehicleSpecification.onlyActive()));
    }

    @Transactional
    public List<VehicleEntity> getByNameOrderedSpecification(final String sortBy) {
        if (Objects.isNull(sortBy))
            throw new IllegalArgumentException("VehicleEntity.getByNameOrdered insufficient name string");
        if(!checkIfColumnExists(sortBy))
            throw new IllegalArgumentException("VehicleEntity.getByNameOrdered There is no" + sortBy + " fild in VehicleEntity");
        return vehicleRepository.findAll(VehicleSpecification.ordered(sortBy));
    }

}
