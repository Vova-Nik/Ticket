package org.hillel.service;

import org.hillel.persistence.entity.TrainRoutesEntity;
import org.hillel.persistence.repository.TrainRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("TrainRouteService")
public class TrainRouteService {
    @Autowired
    private TrainRouteRepository trainRouteRepository;

    @Transactional
    public Long save(final TrainRoutesEntity entity){
        if(entity==null||!entity.isValid()) throw new IllegalArgumentException("TrainRouteService.create - TrainRoutesEntity is not valid");
            return trainRouteRepository.create(entity);
    }
}
