package org.hillel;

import org.hillel.exceptions.OveralException;
import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.VehicleEntity;
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
    StationService stationService;

    public void createStationsPool() {

        StationEntity ods = new StationEntity("Odessa");
        ods.setStopType(StationType.TRANSIT);
        Long stopid = stationService.createStation(ods);

        StationEntity kyiv = new StationEntity("Kyiv");
        kyiv.setStopType(StationType.TRANSIT);
        stationService.createStation(kyiv);

        StationEntity lviv = new StationEntity("Lviv");
        lviv.setStopType(StationType.TRANSIT);
        stationService.createStation(lviv);

        StationEntity dnepr = new StationEntity("Dnepr");
        dnepr.setStopType(StationType.TRANSIT);
        stationService.createStation(dnepr);

        stationService.createStation(new StationEntity("Gmerinka"));
        stationService.createStation(new StationEntity("Vapnyarka"));
        stationService.createStation(new StationEntity("Fastov"));
        stationService.createStation(new StationEntity("Rozdilna"));
    }

    @Autowired
    RouteService routeService;
    public void createRoutesPool() throws OveralException {
        StationEntity from = stationService.getByName("Odessa");
        StationEntity to = stationService.getByName("Kyiv");
        RouteEntity routeEntity =  new RouteEntity(10,from, to, new Time(20,0,0), new Time(9,0,0));
        StationEntity gm = stationService.getByName("Gmerinka");
        routeEntity.addStation(gm);
        if(!routeEntity.isValid())throw new OveralException("createRoutesPool trainRoutesEntity is not valid");
        routeService.save(routeEntity);

        StationEntity from2 = stationService.getByName("Kyiv");
        StationEntity to2 = stationService.getByName("Odessa");
        RouteEntity routeEntity2 =  new RouteEntity(9,from2, to2, new Time(19,6,0), new Time(6,30,0));
        if(!routeEntity2.isValid())throw new OveralException("createRoutesPool trainRoutesEntity1 is not valid");
        routeService.save(routeEntity2);

        StationEntity from3 = stationService.getByName("Lviv");
        StationEntity to3 = stationService.getByName("Dnepr");
        RouteEntity routeEntity3 =  new RouteEntity(38,from3, to3, new Time(19,6,0), new Time(6,30,0));
        if(!routeEntity3.isValid())throw new OveralException("createRoutesPool trainRoutesEntity1 is not valid");
        routeService.save(routeEntity3);
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
        if(odessa==null||kyiv==null||!ve.isPresent()) throw new OveralException("Journey creation error");
        JourneyEntity journeyEntity = new JourneyEntity(odessa,kyiv,ve.get());
        transactionalJourneyService.createJourney(journeyEntity);

        StationEntity lviv = stationService.getByName("Lviv");
        StationEntity dnepr = stationService.getByName("Dnepr");
        Optional<VehicleEntity> ve1 = vehicleService.getByName("Oriental express");
        if(lviv==null||dnepr==null||!ve1.isPresent()) throw new OveralException("Journey creation error");
        JourneyEntity journeyEntity1 = new JourneyEntity(lviv,dnepr,ve1.get());
        transactionalJourneyService.createJourney(journeyEntity1);
    }

}
