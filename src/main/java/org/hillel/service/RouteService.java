package org.hillel.service;

import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("TrainRouteService")
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Transactional
    public Long save(final RouteEntity entity){
        if(entity==null||!entity.isValid()) throw new IllegalArgumentException("TrainRouteService.create - TrainRoutesEntity is not valid");
            return routeRepository.createOrUpdate(entity).getId();
    }

    @Transactional
    public RouteEntity getById(final Long id){
        if(id==null||id<0) throw new IllegalArgumentException("TrainRouteService.create - TrainRoutesEntity is not valid");
        return routeRepository.findById(id).orElseThrow(()->new IllegalArgumentException("RouteService.getById - unable to get data by id=" +id));
    }
}
