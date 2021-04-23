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

@Service("StationService")
public class StationService {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private RouteRepository routeRepository;

    @Transactional
    public Long createStation(final StationEntity entity) {
        if (entity == null || !entity.isValid())
            throw new IllegalArgumentException("StopService.createStop - StationEntity is not valid");
        StationEntity newStation = stationRepository.createOrUpdate(entity);
        return newStation.getId();
    }

    @Transactional
    public StationEntity getByName(final String stationName) {
        return stationRepository.getByName(stationName);
    }

    @Transactional
    public StationEntity getById(final Long id) {
        if (id == null || id < 0) throw new IllegalArgumentException("StationService.create -  id is not valid");
        return stationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("There is no station having id=" + id));
    }

    @Transactional
    public void addRoute(final StationEntity station, final RouteEntity route) {
        if (Objects.isNull(station) || !station.isValid())
            throw new IllegalArgumentException("StationService.addRoute -  station is not valid");
        if (Objects.isNull(route) || !route.isValid())
            throw new IllegalArgumentException("StationService.addRoute -  route is not valid");
        StationEntity se = stationRepository.findById(station.getId()).orElseThrow(() -> new IllegalArgumentException("StationService.addRoute"));
        se.addRoute(route);
    }

    @Transactional
    public void removeById(final Long id) throws UnableToRemove {
        if (id == null || id < 0) throw new IllegalArgumentException("StationService.removeById -  id is not valid");
        StationEntity station = getById(id);
        if (!station.isValid())
            throw new IllegalArgumentException("StationService.removeById -  there is no such station");
        List<RouteEntity> routes = station.getConnectedRoutes();
        for (RouteEntity route : routes) {
            routeRepository.removeStation(route,station);
            stationRepository.removeRoute(station, route);
        }
        stationRepository.removeById(id);
    }

    @Transactional
    public boolean exists(Long id) {
        return stationRepository.exists(id);
    }
}
