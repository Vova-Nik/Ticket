package org.hillel.service;

import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("TrainRouteService")
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Transactional
    public Long save(final RouteEntity entity){
        if(entity==null||!entity.isValid()) throw new IllegalArgumentException("TrainRouteService.create - TrainRoutesEntity is not valid");
            return routeRepository.create(entity);
    }
}
