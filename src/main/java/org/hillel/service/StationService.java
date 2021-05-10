package org.hillel.service;

import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.jpa.repository.StationJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service("StationService")
public class StationService  extends EntityServiceImplementation<StationEntity, Long>{

    private final StationJPARepository stationRepository;
//    @Autowired
//    private RouteJPARepository routeRepository;

    @Autowired
    public StationService(StationJPARepository stationRepository){
        super(StationEntity.class, stationRepository);
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public boolean containsRoute(final StationEntity station, final Long routeId ){
        if (Objects.isNull(station) ||Objects.isNull(station.getId()) || Objects.isNull(routeId)) throw new IllegalArgumentException("RouteService.containsStation bad input");
        StationEntity st = stationRepository.findById(station.getId()).orElseThrow(()->new IllegalArgumentException("RouteService.containsStation can not find route"));
        return st.containsRoute(routeId);
    }

    @Transactional
    public void addRoute(final StationEntity station, final RouteEntity route) {
        if (Objects.isNull(station) || !station.isValid() || Objects.isNull(station.getId()))
            throw new IllegalArgumentException("StationService.addRoute -  station is not valid");
        if (Objects.isNull(route) || !route.isValid() ||  Objects.isNull(route.getId()))
            throw new IllegalArgumentException("StationService.addRoute -  route is not valid");
        StationEntity st = stationRepository.findById(station.getId()).orElseThrow(()->new IllegalArgumentException("StationService.addRoute -  station is not valid"));
        st.addRoute(route);
    }

    @Transactional(readOnly = true)
    public void removeRoute(final StationEntity station, final RouteEntity route) {
        if (Objects.isNull(station) || Objects.isNull(station.getId()))
            throw new IllegalArgumentException("StationService.addRoute -  station is not valid");
        if (Objects.isNull(route) || Objects.isNull(route.getId()) || !route.isValid())
            throw new IllegalArgumentException("StationService.addRoute -  route is not valid");
        StationEntity st = stationRepository.findById(station.getId()).orElseThrow(()->new IllegalArgumentException("StationService.removeRoute -  station is not valid"));
        st.removeRoute(route);
    }

    @Override
    boolean isValid(StationEntity entity) {
        return entity.isValid();
    }

}
