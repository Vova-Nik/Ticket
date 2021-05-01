package org.hillel;

import org.hillel.persistence.entity.*;
import org.hillel.persistence.entity.enums.StationType;
import org.hillel.persistence.entity.enums.VehicleType;
import org.hillel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component("TablesCreator")
public class TablesCreator {

    @Autowired
    ClientService clientService;

    public void createClients() {
        clientService.save(new ClientEntity("John", "Smith"));
        clientService.save(new ClientEntity("John", "Viskey"));
        clientService.save(new ClientEntity("John", "Djin"));
    }

    @Autowired
    StationService stationService;

    public void createStationsPool(){

        StationEntity ods = new StationEntity("Odessa");
        ods.setStationType(StationType.TRANSIT);
        Long stopid = stationService.createStation(ods);

        StationEntity kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        stationService.createStation(kyiv);

        StationEntity lviv = new StationEntity("Lviv");
        lviv.setStationType(StationType.TRANSIT);
        stationService.createStation(lviv);

        StationEntity dnepr = new StationEntity("Dnepr");
        dnepr.setStationType(StationType.TRANSIT);
        stationService.createStation(dnepr);

        stationService.createStation(new StationEntity("Gmerinka"));
        stationService.createStation(new StationEntity("Vapnyarka"));
        stationService.createStation(new StationEntity("Fastov"));
        stationService.createStation(new StationEntity("Rozdilna"));

        System.out.println(stationService.getById(1L));
    }

    @Autowired
    RouteService routeService;
    long routeId1;
    long routeId2;
    long routeId3;

    public void createRoutesPool(){
        StationEntity from = stationService.getByName("Odessa");
        StationEntity to = stationService.getByName("Kyiv");
        RouteEntity routeEntity = new RouteEntity("10", from, to, new Time(20, 0, 0), 30000);
        StationEntity gm = stationService.getByName("Gmerinka");
        routeEntity.addStation(gm);
        if (!routeEntity.isValid()) throw new IllegalArgumentException("createRoutesPool trainRoutesEntity is not valid");
        routeId1 = routeService.save(routeEntity);

        StationEntity from2 = stationService.getByName("Kyiv");
        StationEntity to2 = stationService.getByName("Odessa");
        RouteEntity routeEntity2 = new RouteEntity("9", from2, to2, new Time(19, 6, 0), 29000);
        if (!routeEntity2.isValid()) throw new IllegalArgumentException("createRoutesPool trainRoutesEntity1 is not valid");
        routeId2 = routeService.save(routeEntity2);

        StationEntity from3 = stationService.getByName("Lviv");
        StationEntity to3 = stationService.getByName("Dnepr");
        RouteEntity routeEntity3 = new RouteEntity("38", from3, to3, new Time(19, 6, 0),40000);
        if (!routeEntity3.isValid()) throw new IllegalArgumentException("createRoutesPool trainRoutesEntity1 is not valid");
        routeId3 = routeService.save(routeEntity3);
    }

    @Autowired
    VehicleService vehicleService;
    List<VehicleEntity> vehicles = new ArrayList<>();

    public void createVehiclesPool() {
        vehicles.add(new VehicleEntity("Chernomoretc", VehicleType.TRAIN));
        vehicles.add(new VehicleEntity("Green train", VehicleType.TRAIN));
        vehicles.add(new VehicleEntity("Oriental express", VehicleType.TRAIN));
        vehicles.add(new VehicleEntity("UFO express", VehicleType.TRAIN));
        vehicles.add(new VehicleEntity("Red Arrow", VehicleType.TRAIN));
        for (VehicleEntity vehicle : vehicles
        ) {
            vehicleService.save(vehicle);
        }
    }

    @Autowired
    @Qualifier("transactionalJourneyService")
    JourneyService transactionalJourneyService;
    List<JourneyEntity> journeys = new ArrayList<>();

    public void createJourneys(){
        StationEntity odessa = stationService.getByName("Odessa");
        StationEntity kyiv = stationService.getByName("Kyiv");
        if (odessa == null || kyiv == null) throw new IllegalArgumentException("Journey creation error");
        RouteEntity route1 = routeService.getById(routeId1);
        JourneyEntity journeyEntity0 = new JourneyEntity(route1, odessa, kyiv, LocalDate.now());
        transactionalJourneyService.createJourney(journeyEntity0);
//        transactionalJourneyService.setVehicle(journeyEntity0,vehicles.get(0));

        RouteEntity route2 = routeService.getById(routeId2);
        JourneyEntity journeyEntity1 = new JourneyEntity(route2, kyiv, odessa, LocalDate.now());
        transactionalJourneyService.createJourney(journeyEntity1);
//        transactionalJourneyService.setVehicle(journeyEntity1,vehicles.get(0));

        StationEntity lviv = stationService.getByName("Lviv");
        StationEntity dnepr = stationService.getByName("Dnepr");
        if (lviv == null || dnepr == null) throw new IllegalArgumentException("Journey creation error");

        RouteEntity route3 = routeService.getById(routeId3);
        JourneyEntity journeyEntity2 = new JourneyEntity(route3, dnepr, lviv, LocalDate.now());
        transactionalJourneyService.createJourney(journeyEntity2);
//        transactionalJourneyService.setVehicle(journeyEntity2,vehicles.get(1));

        JourneyEntity journeyEntity3 = new JourneyEntity(route3, lviv, dnepr, LocalDate.now());
        transactionalJourneyService.createJourney(journeyEntity3);
//        transactionalJourneyService.setVehicle(journeyEntity3,vehicles.get(1));

        journeys.add(journeyEntity0);
        journeys.add(journeyEntity1);
        journeys.add(journeyEntity2);
        journeys.add(journeyEntity3);
    }





}
