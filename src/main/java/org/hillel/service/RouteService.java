package org.hillel.service;

import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.jpa.repository.RouteJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service("TrainRouteService")
public class RouteService extends EntityServiceImplementation<RouteEntity, Long> {

    private final RouteJPARepository routeRepository;

    @Autowired
    public RouteService(final RouteJPARepository routeRepository) {
        super(RouteEntity.class, routeRepository);
        this.routeRepository = routeRepository;
    }

    @Override
    boolean isValid(RouteEntity entity) {
        return entity.isValid();
    }

    @Transactional(readOnly = true)
    public boolean containsStation(final Long routeId, final StationEntity station) {
        if (Objects.isNull(station) || Objects.isNull(routeId)) return false;
        RouteEntity route = routeRepository.findById(routeId).orElseThrow(()->new IllegalArgumentException("RouteService.containsStation can not find route"));
        return route.containsStation(station);
    }

    @Transactional
    public void addStation(final Long routeId, final StationEntity station) {
        if (Objects.isNull(station) || Objects.isNull(routeId) || !station.isValid()) throw new IllegalArgumentException("RouteService addStation bad data");
        RouteEntity route = routeRepository.findById(routeId).orElseThrow(()->new IllegalArgumentException("RouteService.containsStation can not find route"));
        if(route.getStations().contains(station)) return;
        route.addStation(station);
        routeRepository.save(route);
    }

    @Transactional
    public void removeStation(final Long routeId, final StationEntity station) throws UnableToRemove {
        if (Objects.isNull(station) || Objects.isNull(routeId)) throw new IllegalArgumentException("RouteService addStation bad data");
        RouteEntity route = routeRepository.findById(routeId).orElseThrow(()->new IllegalArgumentException("RouteService.containsStation can not find route"));
        if(!route.getStations().contains(station)) return;
        route.removeStation(station);
        routeRepository.save(route);
    }


}
