package org.hillel.service;

import org.hillel.config.RootConfig;
import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.entity.VehicleEntity;
import org.hillel.persistence.entity.enums.StationType;
import org.hillel.persistence.entity.enums.VehicleType;
import org.junit.jupiter.api.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

class RouteServiceTest {
    static ConfigurableApplicationContext applicationContext;
    static Environment env;
    static StationService stationService;
    static RouteService routeService;
    static long routeId1;
    static long routeId2;
    static RouteEntity routeEntity1;
    static RouteEntity routeEntity2;
    static StationEntity ods;
    static StationEntity kyiv;
    static StationEntity gmerinka;

    @BeforeAll
    static void config() {
        applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        stationService = applicationContext.getBean(org.hillel.service.StationService.class);
        routeService = applicationContext.getBean(RouteService.class);

        ods = new StationEntity("Odessa");
        ods.setStationType(StationType.TRANSIT);
        Long stopid = stationService.save(ods).getId();
        kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        stationService.save(kyiv);
        gmerinka = new StationEntity("Gmerinka");
        gmerinka.setStationType(StationType.TRANSIT);
        stationService.save(gmerinka);
    }

    @BeforeEach
    void setUp() {
        routeEntity1 = new RouteEntity("10", ods, kyiv, new Time(20, 27, 0), 29800);
        routeEntity1 = routeService.save(routeEntity1);
        assertNotNull(routeEntity1.getId());
        routeId1 = routeEntity1.getId();

        routeEntity2 = new RouteEntity("110", ods, kyiv, new Time(18, 05, 0), 30800);
        routeEntity2 = routeService.save(routeEntity2);
        assertNotNull(routeEntity2.getId());
        routeId2 = routeEntity2.getId();
    }

    @Test
    void containsStation() {
        assertTrue(routeService.containsStation(routeEntity1.getId(), kyiv));
        assertFalse(routeService.containsStation(routeEntity1.getId(), gmerinka));
    }

    @Test
    void addStation() {
        assertTrue(routeService.containsStation(routeEntity1.getId(), kyiv));
        assertFalse(routeService.containsStation(routeEntity1.getId(), gmerinka));
        routeService.addStation(routeEntity1.getId(), gmerinka);
        assertTrue(routeService.containsStation(routeEntity1.getId(), gmerinka));
    }

    @Test
    void removeStation() {
        assertTrue(routeService.containsStation(routeEntity1.getId(), kyiv));
        assertFalse(routeService.containsStation(routeEntity1.getId(), gmerinka));
        routeService.addStation(routeEntity1.getId(), gmerinka);
        assertTrue(routeService.containsStation(routeEntity1.getId(), gmerinka));
        try {
            routeService.removeStation(routeEntity1.getId(), kyiv);
            fail();
        } catch (UnableToRemove e) {
            assertTrue(routeService.containsStation(routeEntity1.getId(), kyiv));
        }
        try {
            routeService.removeStation(routeEntity1.getId(), gmerinka);
            assertFalse(routeService.containsStation(routeEntity1.getId(), gmerinka));
        } catch (UnableToRemove e) {
            fail();
        }
    }

    @AfterEach
    void Clear() {
        routeService.deleteAll();
    }

    @AfterAll
    static void tearDown() {
        applicationContext.close();
    }
}