package org.hillel.service;

import org.hillel.config.RootConfig;
import org.hillel.exceptions.UnableToRemove;
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

import java.sql.Time;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class StationServiceTest {

    static ConfigurableApplicationContext applicationContext;
    static Environment env;

    static StationService stationService;
    static RouteService routeService;
    static VehicleService vehicleService;
    static JourneyService journeyService;

    static long routeId1;
    static long routeId2;
    static RouteEntity routeEntity1;
    static RouteEntity routeEntity2;
    static VehicleEntity vehicle1;
    static VehicleEntity vehicle2;
    static JourneyEntity journeyEntity1;
    static JourneyEntity journeyEntity2;
    static StationEntity ods;
    static StationEntity kyiv;

    @BeforeAll
    public static void setUp() {

        applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        journeyService = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), JourneyService.class, "transactionalJourneyService");
        vehicleService = applicationContext.getBean(VehicleService.class);
        stationService = applicationContext.getBean(org.hillel.service.StationService.class);
        routeService = applicationContext.getBean(RouteService.class);

        vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);

        ods = new StationEntity("Odessa");
        ods.setStationType(StationType.TRANSIT);
        Long stopid = stationService.createStation(ods);

        kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        stationService.createStation(kyiv);

        routeEntity1 = new RouteEntity("10", ods, kyiv, new Time(20, 0, 0), new Time(9, 0, 0));
        routeId1 = routeService.save(routeEntity1);
        routeEntity2 = new RouteEntity("9", kyiv, ods, new Time(19, 6, 0), new Time(6, 30, 0));
        routeId2 = routeService.save(routeEntity2);
    }

    @Test
    void removeById() {

        StationEntity rovno = new StationEntity("Rovno");
        stationService.createStation(rovno);
        stationService.addRoute(rovno, routeEntity1);
        routeService.addStation(routeEntity1, rovno);


        journeyEntity1 = new JourneyEntity(routeEntity1, rovno, kyiv);
        Long journey1Id = journeyService.createJourney(journeyEntity1);
        journeyService.setVehicle(journeyEntity1, vehicle1);

        assertTrue(journeyService.exists(journey1Id));
        assertTrue(stationService.exists(rovno.getId()));
        assertTrue(routeService.containsStation(routeEntity1,rovno));
        try {
            stationService.removeById(rovno.getId());
        } catch (UnableToRemove e) {
            System.out.println(e.getMessage());
            fail();
        }
        assertFalse(routeService.containsStation(routeEntity1,rovno));
        assertFalse(stationService.exists(rovno.getId()));
        assertFalse(journeyService.exists(journey1Id));
        rovno=null;

        try {
            stationService.removeById(ods.getId());
            fail();
        } catch (UnableToRemove e) {
            assertTrue(e.getMessage().length()>2);
        }

    }

    @AfterAll
    public static void unSetUp() {
        applicationContext.close();
    }
}