package org.hillel.service;

import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.entity.StationEntity_;
import org.hillel.persistence.repository.RouteRepository;
import org.hillel.persistence.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Transactional(readOnly = true)
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

    @Transactional(propagation = Propagation.REQUIRED)
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

    @Transactional(readOnly = true)
    public StationEntity getByNameActive(String name){
        List<StationEntity> stations =  stationRepository.findByNameActive(name);
         if(stations.size()>1) throw new IllegalArgumentException("There more then 1 Stations with name " + name);
        if(stations.size()==0) return new StationEntity();
        return stations.get(0);
    }

    @Transactional(readOnly = true)
    public StationEntity getByName(final String name) {
        List<StationEntity> stations =  stationRepository.findByName(name);
        if(stations.size()>1) throw new IllegalArgumentException("There more then 1 Stations with name " + name);
        if(stations.size()==0) return new StationEntity();
        return stations.get(0);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<StationEntity> getAll() {
        List<StationEntity> stations = new ArrayList<>();
        Optional<Collection<StationEntity>> oStations =  stationRepository.findAll();
        return oStations.map(stationEntities -> (List<StationEntity>) stationEntities).orElse(stations);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        return stationRepository.exists(id);
    }

    @Transactional(readOnly = true)
    public List<StationEntity> getSortedByPage(int pageSize, int first, String sortBy){
        if (!checkSortingCriteria(sortBy))
            throw new IllegalArgumentException("StationService.getSorted insufficient sortBy parameter");
        return stationRepository.getSortedByPage(pageSize, first, sortBy, true).orElseGet(ArrayList::new);
    }

    @Transactional(readOnly = true)
    public List<StationEntity> getSorted(String sortBy){
        if (!checkSortingCriteria(sortBy))
            throw new IllegalArgumentException("StationService.getSorted insufficient sortBy parameter");
        return stationRepository.getSorted(sortBy, true).orElseGet(ArrayList::new);
    }

    boolean checkSortingCriteria(String sortBy) {
        return (sortBy.equals(StationEntity_.ID) || sortBy.equals(StationEntity_.NAME) || sortBy.equals(StationEntity_.ACTIVE) || sortBy.equals(StationEntity_.CREATION_DATE));
    }
}
