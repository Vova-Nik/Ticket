package org.hillel.service;

import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.entity.TripEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class TicketClient {

    @Autowired
    private JourneyService journeyService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private StationService stationService;
    @Autowired
    private TripService tripService;

    public TicketClient() {
    }

    public List<JourneyEntity> find(String stationFrom, String stationTo, LocalDate date) {

        List<JourneyEntity> resultList = new ArrayList<>();
        StationEntity from;
        StationEntity to;
        Set<Long> fromRoutes = stationService.getConnectedRoutesIds(stationFrom);
        Set<Long> toRoutes = stationService.getConnectedRoutesIds(stationTo);
        Set<Long> intersection = new HashSet<>(fromRoutes);
        if (intersection.retainAll(toRoutes)) {
            try {
                 from = stationService.findOneByName(stationFrom);
                 to = stationService.findOneByName(stationTo);
            }catch (IncorrectResultSizeDataAccessException e) {
                throw new IllegalArgumentException("Station is double. Message system administrator");
            }
            if(Objects.isNull(from)){
                throw new IllegalArgumentException("there is no station with name " + stationFrom);
            }
            if(Objects.isNull(to)){
                throw new IllegalArgumentException("there is no station with name " + stationTo);
            }

            Long[] routeIds = (Long[]) intersection.toArray();
            List<RouteEntity> routes = routeService.findByIds(routeIds);
            for (RouteEntity route : routes
            ) {
                resultList.add(new JourneyEntity(route, from, to, date));
            }
        }
        //to do
        //find or if it necessary create correspondent Trip with received date
            return resultList;

    }
}


