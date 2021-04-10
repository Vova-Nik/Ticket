package org.hillel;

import org.hillel.exceptions.OveralException;
import org.hillel.persistence.entity.*;
import org.hillel.persistence.entity.enums.StationType;
import org.hillel.persistence.entity.enums.VehicleType;
import org.hillel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Optional;

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

    public void createStationsPool() throws OveralException {

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

    public void createRoutesPool() throws OveralException {
        StationEntity from = stationService.getByName("Odessa");
        StationEntity to = stationService.getByName("Kyiv");
        RouteEntity routeEntity = new RouteEntity("10", from, to, new Time(20, 0, 0), new Time(9, 0, 0));
        StationEntity gm = stationService.getByName("Gmerinka");
        routeEntity.addStation(gm);
        if (!routeEntity.isValid()) throw new OveralException("createRoutesPool trainRoutesEntity is not valid");
        routeId1 = routeService.save(routeEntity);

        StationEntity from2 = stationService.getByName("Kyiv");
        StationEntity to2 = stationService.getByName("Odessa");
        RouteEntity routeEntity2 = new RouteEntity("9", from2, to2, new Time(19, 6, 0), new Time(6, 30, 0));
        if (!routeEntity2.isValid()) throw new OveralException("createRoutesPool trainRoutesEntity1 is not valid");
        routeId2 = routeService.save(routeEntity2);

        StationEntity from3 = stationService.getByName("Lviv");
        StationEntity to3 = stationService.getByName("Dnepr");
        RouteEntity routeEntity3 = new RouteEntity("38", from3, to3, new Time(19, 6, 0), new Time(6, 30, 0));
        if (!routeEntity3.isValid()) throw new OveralException("createRoutesPool trainRoutesEntity1 is not valid");
        routeId3 = routeService.save(routeEntity3);
    }

    @Autowired
    VehicleService vehicleService;

    public void createVehiclesPool() {
        VehicleEntity trainVehicle = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(trainVehicle);
        trainVehicle = new VehicleEntity("Oriental express", VehicleType.TRAIN);
        vehicleService.save(trainVehicle);
        trainVehicle = new VehicleEntity("UFO express", VehicleType.TRAIN);
        vehicleService.save(trainVehicle);
        trainVehicle = new VehicleEntity("Red Arrow", VehicleType.TRAIN);
        vehicleService.save(trainVehicle);
        trainVehicle = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(trainVehicle);
    }

    @Autowired
    @Qualifier("transactionalJourneyService")
    JourneyService transactionalJourneyService;

    public void createJourneys() throws OveralException {
        StationEntity odessa = stationService.getByName("Odessa");
        StationEntity kyiv = stationService.getByName("Kyiv");
        Optional<VehicleEntity> ve = vehicleService.getByName("Chernomoretc");
        if (odessa == null || kyiv == null || !ve.isPresent()) throw new OveralException("Journey creation error");
        RouteEntity route1 = routeService.getById(routeId1);
        JourneyEntity journeyEntity = new JourneyEntity(route1, odessa, kyiv, ve.get());
        transactionalJourneyService.createJourney(journeyEntity);

        StationEntity lviv = stationService.getByName("Lviv");
        StationEntity dnepr = stationService.getByName("Dnepr");
        Optional<VehicleEntity> ve1 = vehicleService.getByName("Oriental express");
        if (lviv == null || dnepr == null || !ve1.isPresent()) throw new OveralException("Journey creation error");
        RouteEntity route3 = routeService.getById(routeId3);
        JourneyEntity journeyEntity1 = new JourneyEntity(route3, lviv, dnepr, ve1.get());
        transactionalJourneyService.createJourney(journeyEntity1);
    }

}
