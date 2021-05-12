package org.hillel.service;

import org.hillel.config.RootConfig;
import org.hillel.persistence.entity.*;
import org.hillel.persistence.entity.enums.StationType;
import org.hillel.persistence.entity.enums.VehicleType;
import org.junit.jupiter.api.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import java.time.Instant;
import java.time.LocalDate;
import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TripServiceTest {
    static ConfigurableApplicationContext applicationContext;
    static Environment env;

    static StationService stationService;
    static RouteService routeService;
    static VehicleService vehicleService;
    static JourneyService journeyService;
    static TripService tripService;

    static long routeId1;
    static long routeId2;
    static RouteEntity routeEntity1, routeEntity2, routeEntity3;

    static VehicleEntity vehicle1;
    static VehicleEntity vehicle2;
    static JourneyEntity journeyEntity1;
    static JourneyEntity journeyEntity2;
    static StationEntity ods;
    static StationEntity kyiv;

    @BeforeAll
    static void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        //        journeyService = applicationContext.getBean(JourneyService.class);
        vehicleService = applicationContext.getBean(VehicleService.class);
        stationService = applicationContext.getBean(org.hillel.service.StationService.class);
        routeService = applicationContext.getBean(RouteService.class);
        tripService = applicationContext.getBean(TripService.class);

        vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        vehicle2 = new VehicleEntity("Just Bus", VehicleType.BUS);
        vehicleService.save(vehicle2);

        ods = new StationEntity("Odessa");
        ods.setStationType(StationType.TRANSIT);
        Long stopid = stationService.save(ods).getId();

        kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        stationService.save(kyiv);

        routeEntity1 = new RouteEntity("10", ods, kyiv, new Time(20, 27, 0), 29800);
        routeEntity1 = routeService.save(routeEntity1);
        assertNotNull(routeEntity1.getId());
        routeId1 = routeEntity1.getId();

        routeEntity2 = new RouteEntity("110", ods, kyiv, new Time(18, 5, 0), 30800);
        routeEntity2 = routeService.save(routeEntity2);
        assertNotNull(routeEntity2.getId());
        routeId2 = routeEntity2.getId();

        routeEntity3 = new RouteEntity("11", kyiv, ods, new Time(21, 15, 0), 29300);
        routeEntity3 = routeService.save(routeEntity3);
        assertNotNull(routeEntity3.getId());

    }

    @BeforeEach
    void start() {
    }

    @Test
    void findByRouteAndDate() {
        LocalDate date = LocalDate.now().plusDays(11);
        TripEntity trip1 = new TripEntity(routeEntity1, vehicle1, date);
        tripService.save(trip1);
        LocalDate date1 = LocalDate.now().plusDays(12);
        TripEntity trip2 = new TripEntity(routeEntity1, vehicle1, date1);
        tripService.save(trip2);
        date1 = LocalDate.now().plusDays(13);
        TripEntity trip3 = new TripEntity(routeEntity1, vehicle1, date1);
        tripService.save(trip3);
        date1 = LocalDate.now().plusDays(14);
        TripEntity trip4 = new TripEntity(routeEntity2, vehicle1, date1);
        tripService.save(trip4);
        date1 = LocalDate.now().plusDays(15);
        TripEntity trip5 = new TripEntity(routeEntity2, vehicle2, date);
        tripService.save(trip5);

        List<TripEntity> trips = tripService.findByRouteAndDate(routeEntity1.getId(), date);
        assertEquals(trips.get(0), trip1);
        assertEquals(1, trips.size());

        tripService.disableById(trip1.getId());
        trips = tripService.findByRouteAndDateActive(routeEntity1.getId(), date);
        assertEquals(0, trips.size());
    }

    @Test
    void constrainsTest() {
        LocalDate date = LocalDate.now().plusWeeks(3);
        TripEntity trip1 = new TripEntity(routeEntity1, vehicle1, date);
        tripService.save(trip1);
        try {
            TripEntity trip2 = new TripEntity(routeEntity1, vehicle1, date);
            tripService.save(trip2);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.toString().contains("TripService.save such trip already exists"));
        }
    }

    @Test
    void getDeparture() {
        RouteEntity routeEntity = new RouteEntity("75", kyiv, ods, new Time(23, 6, 0), 30000);
        Long routeId = routeService.save(routeEntity).getId();
        LocalDate date = LocalDate.now().plusMonths(1);
        TripEntity trip = new TripEntity(routeEntity, vehicle1, date);
        tripService.save(trip);
        Instant ins = trip.getDeparture();
        assertTrue(ins.toString().contains("T23:06:00Z"));
    }

    @Test
    void sellTicket() {
        RouteEntity routeEntity = new RouteEntity("75", kyiv, ods, new Time(23, 6, 0), 30000);
        Long routeId = routeService.save(routeEntity).getId();

        LocalDate date = LocalDate.now().plusWeeks(1);
        TripEntity trip = new TripEntity(routeEntity, vehicle1, date);
        tripService.save(trip);

        int ticketsFree, ticetsOveral;
        ticetsOveral = tripService.getFree(trip.getId());
        tripService.sellTicket(trip.getId());
        ticketsFree = tripService.getFree(trip.getId());

        assertEquals(ticetsOveral, (ticketsFree + 1));
        tripService.sellTicket(trip.getId());
        tripService.sellTicket(trip.getId());
        ticketsFree = tripService.getFree(trip.getId());
        assertEquals(ticetsOveral, (ticketsFree + 3));
    }

    @Test
    void findByRootSpecification() {
        LocalDate date = LocalDate.now().plusDays(11);
        TripEntity trip1 = new TripEntity(routeEntity1, vehicle1, date);
        tripService.save(trip1);
        LocalDate date1 = LocalDate.now().plusDays(12);
        TripEntity trip2 = new TripEntity(routeEntity1, vehicle1, date1);
        tripService.save(trip2);
        date1 = LocalDate.now().plusDays(13);
        TripEntity trip3 = new TripEntity(routeEntity1, vehicle1, date1);
        tripService.save(trip3);
        date1 = LocalDate.now().plusDays(14);
        TripEntity trip4 = new TripEntity(routeEntity2, vehicle1, date1);
        tripService.save(trip4);
        date1 = LocalDate.now().plusDays(15);
        TripEntity trip5 = new TripEntity(routeEntity2, vehicle2, date);
        tripService.save(trip5);

        List<TripEntity> trips = tripService.getByrootSpec(routeEntity1.getId());
        assertEquals(3, trips.size());
        assertTrue(trips.get(0).getRoute().equals(routeEntity1) && trips.get(1).getRoute().equals(routeEntity1) && trips.get(1).getRoute().equals(routeEntity1));
        trips = tripService.getByrootSpec(routeEntity2.getId());
        assertEquals(2, trips.size());
        assertTrue(trips.get(0).getRoute().equals(routeEntity2) && trips.get(1).getRoute().equals(routeEntity2));
        System.out.println(trips);
    }

    @Test
    void findByRootDaySpecification() {
        LocalDate date1 = LocalDate.now().plusDays(1);
        LocalDate date2 = LocalDate.now().plusDays(2);
        TripEntity trip1 = new TripEntity(routeEntity1, vehicle1, date1);
        tripService.save(trip1);
        TripEntity trip2 = new TripEntity(routeEntity1, vehicle1, date2);
        tripService.save(trip2);
        TripEntity trip3 = new TripEntity(routeEntity2, vehicle2, date1);
        tripService.save(trip3);
        TripEntity trip4 = new TripEntity(routeEntity2, vehicle1, date2);
        tripService.save(trip4);
        TripEntity trip5 = new TripEntity(routeEntity3, vehicle2, date1);
        tripService.save(trip5);
        TripEntity trip6 = new TripEntity(routeEntity3, vehicle2, date2);
        tripService.save(trip6);

        List<TripEntity> trips = tripService.getByDateSpec(date2);
        assertEquals(3, trips.size());
        assertTrue(trips.contains(trip2) && trips.contains(trip4) && trips.contains(trip6));

        trips = tripService.getByRootDateSpec(routeEntity1.getId(), date2);
        assertEquals(1, trips.size());
        assertEquals(trip2,trips.get(0));

        LocalDate date3 = LocalDate.now().plusDays(12);
        trips = tripService.getByRootDateSpec(routeEntity1.getId(), date3);
        assertEquals(0, trips.size());

        trips = tripService.getByRootDateSpec(1000L, date2);
        assertEquals(0, trips.size());

    }

    @AfterEach
    void clear() {
        tripService.deleteAll();
    }

    @AfterAll
    static void tearDown() {
        applicationContext.close();
    }
}
