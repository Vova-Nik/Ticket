package org.hillel.service;

import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.repository.RouteRepository;
import org.hillel.persistence.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service("TrainRouteService")
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private StationRepository stationRepository;

    @Transactional
    public Long save(final RouteEntity route) {
        if (route == null || !route.isValid())
            throw new IllegalArgumentException("TrainRouteService.create - TrainRoutesEntity is not valid");
        stationRepository.addRoute(route.getFromStation(), route);
        stationRepository.addRoute(route.getToStation(), route);
        return routeRepository.createOrUpdate(route).getId();
    }

    @Transactional(readOnly = true)
    public RouteEntity getById(final Long id) {
        if (Objects.isNull(id))
            throw new IllegalArgumentException("TrainRouteService.create - TrainRoutesEntity is not valid");
        return routeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("RouteService.getById - unable to get data by id=" + id));
    }

    @Transactional(readOnly = true)
    public boolean exists(final Long id) {
        return routeRepository.exists(id);
    }

    @Transactional(readOnly = true)
    public boolean containsStation(final RouteEntity route, final StationEntity station) {
        if (Objects.isNull(station) || Objects.isNull(route)) return false;
        return routeRepository.containsStation(route, station);
    }

    @Transactional
    public void addStation(final RouteEntity route, final StationEntity station) {
        if (Objects.isNull(station) || Objects.isNull(route)) throw new IllegalArgumentException("RouteService addStation bad data");
        routeRepository.addStation(route, station);
    }

    @Transactional
    public void removeById(Long id) throws UnableToRemove {
        if (Objects.isNull(id)) throw new IllegalArgumentException("RouteService.removeById bad id");
        RouteEntity route = routeRepository.findById(id).orElseThrow(()->new IllegalArgumentException("RouteService.removeById route not found") );
        List<StationEntity> stations = route.getStations();
        for (StationEntity station:stations) {
            stationRepository.removeRoute(station,route);
        }
        routeRepository.removeById(id);
    }
}
