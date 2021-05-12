package org.hillel.service;

import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.jpa.repository.StationJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("StationService")
public class StationService extends EntityServiceImplementation<StationEntity, Long> {

    private final StationJPARepository stationRepository;
//    @Autowired
//    private RouteJPARepository routeRepository;

    @Autowired
    public StationService(StationJPARepository stationRepository) {
        super(StationEntity.class, stationRepository);
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public boolean containsRoute(final StationEntity station, final Long routeId) {
        if (Objects.isNull(station) || Objects.isNull(station.getId()) || Objects.isNull(routeId))
            throw new IllegalArgumentException("RouteService.containsStation bad input");
        StationEntity st = stationRepository.findById(station.getId()).orElseThrow(() -> new IllegalArgumentException("RouteService.containsStation can not find route"));
        return st.containsRoute(routeId);
    }

    @Transactional
    public void addRoute(final StationEntity station, final RouteEntity route) {
        StationEntity st = findById(station.getId());
        st.addRoute(route);
    }

    @Transactional
    public void removeRoute(final StationEntity station, final RouteEntity route) {
        StationEntity st = findById(station.getId());//.orElseThrow(() -> new IllegalArgumentException("StationService.removeRoute -  station is not valid"));
        st.removeRoute(route);
    }

    @Transactional(readOnly = true)
    public Set<Long> getConnectedRoutesIds(String station){
        StationEntity st = stationRepository.findOneByName(station);
        return st.getConnectedRoutesIds();
    }

    @Override
    boolean isValid(StationEntity entity) {
        return entity.isValid();
    }

}
