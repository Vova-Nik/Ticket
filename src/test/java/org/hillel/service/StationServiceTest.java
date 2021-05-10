package org.hillel.service;

import org.hillel.config.RootConfig;
import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.entity.enums.StationType;
import org.junit.jupiter.api.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationServiceTest {

    static ConfigurableApplicationContext applicationContext;
    static Environment env;
    static StationService stationService;
    static RouteService routeService;

    static StationEntity ods, kyiv, barca;
    static RouteEntity routeEntity1, routeEntity2;
    static Long routeId1, routeId2;

    @BeforeAll
    public static void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        stationService = applicationContext.getBean(org.hillel.service.StationService.class);
        routeService = applicationContext.getBean(RouteService.class);

        ods = new StationEntity("Odessa");
        ods.setStationType(StationType.TRANSIT);
        ods = stationService.save(ods);
        kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        kyiv = stationService.save(kyiv);

        barca = new StationEntity("Barcelona");
        barca.setStationType(StationType.TRANSIT);
        barca = stationService.save(kyiv);

        routeEntity1 = new RouteEntity("10", ods, kyiv, new Time(20, 27, 0), 29800);
        routeEntity1 = routeService.save(routeEntity1);
        assertNotNull(routeEntity1.getId());
        routeId1 = routeEntity1.getId();
        stationService.addRoute(ods, routeEntity1);
        stationService.addRoute(kyiv, routeEntity1);

/*        routeEntity2 = new RouteEntity("110", ods, kyiv, new Time(18, 05, 0), 30800);
        routeEntity2 = routeService.save(routeEntity2);
        assertNotNull(routeEntity2.getId());
        routeId2 = routeEntity2.getId();
        stationService.addRoute(ods,routeEntity2);
        stationService.addRoute(kyiv,routeEntity2);*/

    }

    @BeforeEach
    void prepare() {

    }

/*    @Test
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
        routeEntity1 = routeService.save(routeEntity1);
        routeEntity2 = new RouteEntity("9", kyiv, ods, new Time(19, 6, 0), 30500);
        routeEntity2 = routeService.save(routeEntity2);

        routeId1 = routeEntity1.getId();
        routeId2 = routeEntity2.getId();

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
    }*/

    @Test
    void findByName() {
        StationEntity rovno = new StationEntity("Rovno");
        rovno = stationService.save(rovno);

        assertTrue(stationService.exists(rovno.getId()));
        StationEntity st = stationService.findByName("Rovno").get(0);
        assertTrue(st.isValid());
        assertEquals("Rovno", st.getName());
        List<StationEntity> stations = stationService.findByName("xxxxxxx");
        assertEquals(0, stations.size());

        try {
            st = stationService.findByName("").get(0);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name"));
        }
        try {
            stationService.findByName(null);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("name"));
        }
    }


    @Test
    void containsRoute() {
        StationEntity gmerinka;
        gmerinka = new StationEntity("Gmerinka");
        gmerinka.setStationType(StationType.TRANSIT);
        stationService.save(gmerinka);
        assertTrue(stationService.containsRoute(ods, routeEntity1.getId()));
        assertTrue(stationService.containsRoute(kyiv, routeEntity1.getId()));

//        assertTrue(stationService.containsRoute(ods,routeEntity2.getId()));
//        assertTrue(stationService.containsRoute(kyiv,routeEntity2.getId()));
        assertFalse(stationService.containsRoute(gmerinka, routeEntity1.getId()));
//        assertFalse(stationService.containsRoute(gmerinka,routeEntity2.getId()));
        stationService.deleteById(gmerinka.getId());
    }

    @Test
    void addRoute() {
        StationEntity gmerinka;
        StationEntity station;
        Long id;
        gmerinka = new StationEntity("Gmerinka");
        gmerinka.setStationType(StationType.TRANSIT);
        gmerinka = stationService.save(gmerinka);
        id = gmerinka.getId();

        stationService.addRoute(gmerinka, routeEntity1);
        assertTrue(stationService.containsRoute(gmerinka, routeId1));
        station = stationService.findById(id);
        assertTrue(stationService.containsRoute(station, routeId1));
        stationService.deleteById(gmerinka.getId());

    }

    @Test
    void removeRoute() {
        StationEntity gmerinka;

        StationEntity ods11 = stationService.findById(ods.getId());
        StationEntity kyiv11 = stationService.findById(kyiv.getId());
        StationEntity barca11 = stationService.findById(barca.getId());

        gmerinka = new StationEntity("Gmerinka");
        gmerinka.setStationType(StationType.TRANSIT);
        gmerinka = stationService.save(gmerinka);
        Long id = gmerinka.getId();

        assertFalse(stationService.containsRoute(gmerinka, routeId1));

        stationService.addRoute(gmerinka, routeEntity1);
        StationEntity stt = stationService.findById(id);
        boolean res = stationService.containsRoute(gmerinka, routeId1);
        assertTrue(stationService.containsRoute(gmerinka, routeId1));
        gmerinka = stationService.findById(gmerinka.getId());

        stationService.removeRoute(gmerinka, routeEntity1);
        stationService.removeRoute(gmerinka, routeEntity1);
        assertFalse(stationService.containsRoute(gmerinka, routeId1));
//        stationService.removeRoute(gmerinka,routeEntity1);
//        try {
//            stationService.removeById(ods.getId());
//            stationService.removeById(kyiv.getId());
//            stationService.removeById(rovno.getId());
//        } catch (UnableToRemove unableToRemove) {
//            unableToRemove.printStackTrace();
//            fail();
//        }

        stationService.deleteById(gmerinka.getId());
    }


    @AfterEach
    void clear() {


    }

    @AfterAll
    public static void unSetUp() {
        applicationContext.close();
    }
}