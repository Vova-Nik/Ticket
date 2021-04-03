package org.hillel;

import org.hillel.exceptions.OveralException;
import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.entity.TrainRoutesEntity;
import org.hillel.persistence.entity.VehicleEntity;
import org.hillel.persistence.entity.enums.StationType;
import org.hillel.persistence.entity.enums.VehicleType;
import org.hillel.service.StationService;
import org.hillel.service.TrainRouteService;
import org.hillel.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component("TablesCreator")
public class TablesCreator {
//    Map<Long, StationEntity> stations = new HashMap<>();

    @Autowired
    StationService stationService;

    public boolean createStationsPool() {

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
        return true;
    }

    @Autowired
    TrainRouteService trainRouteService;
    public void createRoutesPool() throws OveralException {
        StationEntity from = stationService.getByName("Odessa");
        StationEntity to = stationService.getByName("Kyiv");
        TrainRoutesEntity trainRoutesEntity =  new TrainRoutesEntity(10,from, to, new Time(20,0,0), new Time(9,0,0));
        if(!trainRoutesEntity.isValid())throw new OveralException("createRoutesPool trainRoutesEntity is not valid");
        trainRouteService.save(trainRoutesEntity);

        StationEntity from2 = stationService.getByName("Kyiv");
        StationEntity to2 = stationService.getByName("Odessa");
        TrainRoutesEntity trainRoutesEntity2 =  new TrainRoutesEntity(9,from2, to2, new Time(19,6,0), new Time(6,30,0));
        if(!trainRoutesEntity2.isValid())throw new OveralException("createRoutesPool trainRoutesEntity1 is not valid");
        trainRouteService.save(trainRoutesEntity2);

        StationEntity from3 = stationService.getByName("Lviv");
        StationEntity to3 = stationService.getByName("Dnepr");
        TrainRoutesEntity trainRoutesEntity3 =  new TrainRoutesEntity(38,from2, to2, new Time(19,6,0), new Time(6,30,0));
        if(!trainRoutesEntity2.isValid())throw new OveralException("createRoutesPool trainRoutesEntity1 is not valid");
        trainRouteService.save(trainRoutesEntity2);
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


    public void createJourneys() throws OveralException {
        StationEntity odessa = stationService.getByName("Odessa");
        StationEntity kyiv = stationService.getByName("Kyiv");
        if(odessa==null||kyiv==null) throw new OveralException("Journey creation error");
        JourneyEntity journeyEntity = new JourneyEntity(odessa,kyiv);
    }

}
