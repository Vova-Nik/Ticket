package org.hillel.service;

import org.hillel.persistence.entity.VehicleEntity;
import org.hillel.persistence.entity.enums.VehicleType;
import org.hillel.persistence.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("VehicleService")
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;


    @Transactional
    public Long save(final VehicleEntity entity) {
        if (entity == null || !entity.isValid())
            throw new IllegalArgumentException("StopService.createStop - StopEntity is not valid");
        Long id = vehicleRepository.save(entity);
        return id;
    }

    @Transactional
    public Optional<VehicleEntity> getByName(final String name) {
        if (name == null) throw new IllegalArgumentException("StopService.createStop - StopEntity is not valid");
        return (vehicleRepository.getByName(name));
    }
}
