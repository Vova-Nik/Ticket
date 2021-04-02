package org.hillel;

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

@Component("TablesCreator")
public class TablesCreator {
    @Autowired
    StationService stationService;

    public boolean createStationsPool() {

        StationEntity ods = new StationEntity("Odessa");
        ods.setStopType(StationType.TRANSIT);
        Long stopid = stationService.createStop(ods);

        StationEntity kyiv = new StationEntity("Kyiv");
        kyiv.setStopType(StationType.TRANSIT);
        stationService.createStop(kyiv);

        StationEntity lviv = new StationEntity("Lviv");
        lviv.setStopType(StationType.TRANSIT);
        stationService.createStop(lviv);

        StationEntity dnepr = new StationEntity("Dnepr");
        dnepr.setStopType(StationType.TRANSIT);
        stationService.createStop(dnepr);

        stationService.createStop(new StationEntity("Gmerinka"));
        stationService.createStop(new StationEntity("Vapnyarka"));
        stationService.createStop(new StationEntity("Fastov"));
        stationService.createStop(new StationEntity("Rozdilna"));
        return true;
    }

    @Autowired
    TrainRouteService trainRouteService;
    public void createRoutesPool(){
        StationEntity from = stationService.getByName("Odessa");
        StationEntity to = stationService.getByName("Kyiv");
        TrainRoutesEntity trainRoutesEntity =  new TrainRoutesEntity(10,from, to, new Time(20,0,0), new Time(9,0,0));
        trainRouteService.save(trainRoutesEntity);

        StationEntity from2 = stationService.getByName("Kyiv");
        StationEntity to2 = stationService.getByName("Odessa");
        TrainRoutesEntity trainRoutesEntity2 =  new TrainRoutesEntity(9,from2, to2, new Time(19,6,0), new Time(6,30,0));
        trainRouteService.save(trainRoutesEntity2);
    }

    @Autowired
    VehicleService vehicleService;

    public boolean createVehiclesPool() {

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
        return true;

    }


}
