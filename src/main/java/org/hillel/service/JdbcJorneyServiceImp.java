package org.hillel.service;

import org.hillel.dao.StationRepository;
import org.hillel.dao.RouteRepository;
import org.hillel.model.Journey;
import org.hillel.model.Route;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import java.util.Collections;
import java.util.List;

public class JdbcJorneyServiceImp implements JourneyService {
    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;

    public JdbcJorneyServiceImp(RouteRepository routeRepository, StationRepository stationRepository) {
        this.routeRepository = routeRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Collection<Journey> find(String stationFrom, String stationTo, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
        Collection<Journey> journeys = new ArrayList<>();
        if(!stationRepository.exist(stationFrom) || !stationRepository.exist(stationTo)) return Collections.emptyList();
        List<Route> routes = routeRepository.getRoutes(stationFrom,stationTo);
        for (Route r : routes) {
            journeys.add(new Journey(r.getStationFrom(),r.getStationTo(),r.getDatedDeparture(dateFrom),r.getDatedArrival(dateFrom)));
        }
        return Collections.unmodifiableCollection(journeys);
    }
}
