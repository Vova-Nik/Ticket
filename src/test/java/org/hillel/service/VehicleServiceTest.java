package org.hillel.service;

import org.hillel.config.RootConfig;
import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.entity.VehicleEntity;
import org.hillel.persistence.entity.enums.StationType;
import org.hillel.persistence.entity.enums.VehicleType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleServiceTest {

    static ConfigurableApplicationContext applicationContext;
    static Environment env;

    static StationService stationService;
    static VehicleService vehicleService;
    static VehicleEntity vehicle1;
    static VehicleEntity vehicle2;
    static VehicleEntity vehicle3;

    @BeforeAll
    public static void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        vehicleService = applicationContext.getBean(VehicleService.class);
        stationService = applicationContext.getBean(org.hillel.service.StationService.class);

        vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);
        vehicle3 = new VehicleEntity("Test bus", VehicleType.BUS);
        vehicleService.save(vehicle3);

        assertTrue(vehicleService.exists(vehicle1.getId()));
        assertTrue(vehicleService.exists(vehicle2.getId()));
        assertTrue(vehicleService.exists(vehicle3.getId()));
    }

    @Test
    void getByName() {
    }

    @Test
    void getAllByNamedQuery() {
        List<VehicleEntity> vehicles;
        vehicles = vehicleService.findAllByNamedQuery();
        assertTrue(vehicles.size()>0);
    }

    @Test
    void findAll() {
        Collection<VehicleEntity> vehicles;
        vehicles = vehicleService.findAll();
        assertTrue(vehicles.size()>0);
    }


    @Test
    public void findAllSQL() {
        Collection<VehicleEntity> vehicles;
        vehicles = vehicleService.findAllSQL();
        assertTrue(vehicles.size()>0);
    }

    @Test
    public void findAllCriteria() {
        Collection<VehicleEntity> vehicles;
        vehicles = vehicleService.findAllCriteria();
        assertTrue(vehicles.size()>0);
    }

    @Test
    public void storedProcExecute() {
        VehicleEntity vehicle128 = new VehicleEntity("Bus 128", VehicleType.BUS);
        vehicleService.save(vehicle128);

        Collection<VehicleEntity> vehicles;
        vehicles = vehicleService.storedProcExecute();
        assertEquals(4, vehicles.size());
    }

    @AfterAll
    public static void unSetUp() {
        applicationContext.close();
    }
}