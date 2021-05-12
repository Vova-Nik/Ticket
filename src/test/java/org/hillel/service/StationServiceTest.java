package org.hillel.service;

import org.hillel.config.RootConfig;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.entity.enums.StationType;
import org.junit.jupiter.api.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import java.sql.Time;
import java.util.List;
import java.util.Set;

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
    }

    @Test
    void removeById() {
        StationEntity rovno = new StationEntity("Rovno");
        stationService.save(rovno);
        stationService.addRoute(rovno, routeEntity1);
        assertTrue(stationService.exists(rovno.getId()));
        stationService.deleteById(rovno.getId());
        assertFalse(stationService.exists(rovno.getId()));
    }

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
        stationService.deleteById(rovno.getId());
    }


    @Test
    void containsRoute() {
        StationEntity gmerinka;
        gmerinka = new StationEntity("Gmerinka");
        gmerinka.setStationType(StationType.TRANSIT);
        stationService.save(gmerinka);
        assertTrue(stationService.containsRoute(ods, routeEntity1.getId()));
        assertTrue(stationService.containsRoute(kyiv, routeEntity1.getId()));
        assertFalse(stationService.containsRoute(gmerinka, routeEntity1.getId()));
        stationService.deleteById(gmerinka.getId());
    }

    @Test
    void addRoute() {
        StationEntity gmerinka;
        gmerinka = new StationEntity("Gmerinka");
        gmerinka.setStationType(StationType.TRANSIT);
        gmerinka = stationService.save(gmerinka);
        stationService.addRoute(gmerinka, routeEntity1);
        StationEntity gm1 = stationService.findById(gmerinka.getId());
        assertTrue(stationService.containsRoute(gmerinka, routeId1));
        assertTrue(stationService.containsRoute(gm1, routeId1));
        stationService.deleteById(gmerinka.getId());
    }

    @Test
    void removeRoute() {
        StationEntity gmerinka;
        gmerinka = new StationEntity("Gmerinka");
        gmerinka.setStationType(StationType.TRANSIT);
        gmerinka = stationService.save(gmerinka);
        assertFalse(stationService.containsRoute(gmerinka, routeId1));
        stationService.addRoute(gmerinka, routeEntity1);
        assertTrue(stationService.containsRoute(gmerinka, routeId1));
        stationService.removeRoute(gmerinka, routeEntity1);
        assertFalse(stationService.containsRoute(gmerinka, routeId1));
        stationService.deleteById(gmerinka.getId());
    }

    @Test
    void getConnectedRoutesIds(){
        routeEntity2 = new RouteEntity("10", kyiv, ods, new Time(18, 10, 0), 29000);
        routeEntity2 = routeService.save(routeEntity2);
        assertNotNull(routeEntity2.getId());

        StationEntity gmerinka;
        gmerinka = new StationEntity("Gmerinka");
        gmerinka.setStationType(StationType.TRANSIT);
        gmerinka = stationService.save(gmerinka);

        stationService.addRoute(gmerinka, routeEntity1);
        stationService.addRoute(gmerinka, routeEntity2);
        gmerinka = stationService.findById(gmerinka.getId());
        Set<Long> routes = stationService.getConnectedRoutesIds(gmerinka.getName());
        assertEquals(2, routes.size());
        assertTrue(routes.contains(routeEntity1.getId()));
        assertTrue(routes.contains(routeEntity2.getId()));
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