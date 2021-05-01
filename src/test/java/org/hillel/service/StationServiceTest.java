package org.hillel.service;

import org.hillel.config.RootConfig;
import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.*;
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
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StationServiceTest {

    static ConfigurableApplicationContext applicationContext;
    static Environment env;
    static StationService stationService;
    static RouteService routeService;
    static VehicleService vehicleService;
    static JourneyService journeyService;

    @BeforeAll
    public static void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        journeyService = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), JourneyService.class, "transactionalJourneyService");
        vehicleService = applicationContext.getBean(VehicleService.class);
        stationService = applicationContext.getBean(org.hillel.service.StationService.class);
        routeService = applicationContext.getBean(RouteService.class);
    }

    @Test
    void removeById() {
        long routeId1;
        long routeId2;
        RouteEntity routeEntity1;
        RouteEntity routeEntity2;
        VehicleEntity vehicle1;
        VehicleEntity vehicle2;
        JourneyEntity journeyEntity1;
        JourneyEntity journeyEntity2;

        vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);

        StationEntity ods = new StationEntity("Odessa");
        ods.setStationType(StationType.TRANSIT);
        Long stopid = stationService.createStation(ods);
        StationEntity kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        stationService.createStation(kyiv);

        routeEntity1 = new RouteEntity("10", ods, kyiv, new Time(20, 0, 0), 31000);
        routeId1 = routeService.save(routeEntity1);
        routeEntity2 = new RouteEntity("9", kyiv, ods, new Time(19, 6, 0), 30500);
        routeId2 = routeService.save(routeEntity2);

        StationEntity rovno = new StationEntity("Rovno");
        stationService.createStation(rovno);
        stationService.addRoute(rovno, routeEntity1);
        routeService.addStation(routeEntity1, rovno);

        journeyEntity1 = new JourneyEntity(routeEntity1, rovno, kyiv, LocalDate.now());
        Long journey1Id = journeyService.createJourney(journeyEntity1);
        assertTrue(journeyService.exists(journey1Id));

        assertTrue(stationService.exists(rovno.getId()));
        assertTrue(routeService.containsStation(routeEntity1, rovno));
        try {
            stationService.removeById(rovno.getId());
        } catch (UnableToRemove e) {
            System.out.println(e.getMessage());
            fail();
        }
        assertFalse(routeService.containsStation(routeEntity1, rovno));
        assertFalse(stationService.exists(rovno.getId()));
        assertFalse(journeyService.exists(journey1Id));
        rovno = null;

        try {
            stationService.removeById(ods.getId());
            fail();
        } catch (UnableToRemove e) {
            assertTrue(e.getMessage().length() > 2);
        }
        try {
            routeService.removeById(routeId1);
            routeService.removeById(routeId2);
        } catch (UnableToRemove unableToRemove) {
            unableToRemove.printStackTrace();
            fail();
        }
        try {
            stationService.removeById(ods.getId());
            stationService.removeById(kyiv.getId());
        } catch (UnableToRemove unableToRemove) {
            unableToRemove.printStackTrace();
            fail();
        }
    }

    @Test
    void findByName() {
        StationEntity ods = new StationEntity("Odessa");
        ods.setStationType(StationType.TRANSIT);
        Long stopid = stationService.createStation(ods);
        StationEntity kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        stationService.createStation(kyiv);
        StationEntity rovno = new StationEntity("Rovno");
        stationService.createStation(rovno);
        assertTrue(stationService.exists(rovno.getId()));
        StationEntity st = stationService.getByName("Rovno");
        assertTrue(st.isValid());
        assertEquals("Rovno", st.getName());
        st = stationService.getByName("ccccc");
        assertFalse(st.isValid());
        try {
            st = stationService.getByName("");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name"));
        }
        try {
            stationService.getByName(null);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name"));
        }
        try {
            stationService.removeById(ods.getId());
            stationService.removeById(kyiv.getId());
            stationService.removeById(rovno.getId());
        } catch (UnableToRemove unableToRemove) {
            unableToRemove.printStackTrace();
            fail();
        }
    }

    @Test
    void getSorted() {
        StationEntity ods = new StationEntity("Odessa");
        ods.setStationType(StationType.TRANSIT);
        Long stopid = stationService.createStation(ods);
        StationEntity kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        stationService.createStation(kyiv);
        StationEntity rovno = new StationEntity("Rovno");
        stationService.createStation(rovno);
        assertTrue(stationService.exists(rovno.getId()));

        StationEntity stn;
        for (int i = 0; i < 12; i++) {
            int prefix = (int)(Math.random()*100);
            stn = new StationEntity("Station" + prefix +'_'  +i);
            stationService.createStation(stn);
        }
        assertTrue(stationService.exists(rovno.getId()));
        List<StationEntity> stations = stationService.getSorted(StationEntity_.NAME);
        assertTrue(stations.size()>8);
        int aa = stations.get(0).getName().compareTo(stations.get(8).getName());
        assertTrue(aa<0);
        print(stations);
        stations = stationService.getSorted(StationEntity_.ID);
        assertTrue(stations.size()>8);
        assertTrue(stations.get(0).getId()<stations.get(7).getId());
        print(stations);

        try {
            for (StationEntity station:stations
                 ) {
                stationService.removeById(station.getId());
            }
        } catch (UnableToRemove unableToRemove) {
            unableToRemove.printStackTrace();
            fail();
        }
    }

    @Test
    void getSortedByPage() {
        StationEntity ods = new StationEntity("Odessa");
        ods.setStationType(StationType.TRANSIT);
        Long stopid = stationService.createStation(ods);
        StationEntity kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        stationService.createStation(kyiv);

        StationEntity stn;
        for (int i = 0; i < 12; i++) {
            int prefix = (int)(Math.random()*100);
            stn = new StationEntity("Station" + prefix +'_'  +i);
            stationService.createStation(stn);
        }
        List<StationEntity> stations = stationService.getSortedByPage(5,1, StationEntity_.NAME);
        assertEquals(5, stations.size());
        assertEquals("Odessa", stations.get(0).getName());
        print(stations);
        try {
            for (StationEntity station:stations
            ) {
                stationService.removeById(station.getId());
            }
        } catch (UnableToRemove unableToRemove) {
            unableToRemove.printStackTrace();
            fail();
        }
    }

    void print(List<StationEntity> stations){
        System.out.println("------------------------------------------------------------");
        for (StationEntity station:stations
        ) {
            System.out.println(station);
        }
        System.out.println("------------------------------------------------------------");
    }

    @AfterAll
    public static void unSetUp() {
        applicationContext.close();
    }
}