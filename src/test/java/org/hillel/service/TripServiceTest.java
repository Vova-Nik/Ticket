package org.hillel.service;

import org.hillel.config.RootConfig;
import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.*;
import org.hillel.persistence.entity.enums.StationType;
import org.hillel.persistence.entity.enums.VehicleType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import javax.persistence.PersistenceException;
import java.time.Instant;
import java.time.LocalDate;
import java.sql.Time;
import java.util.ArrayList;
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
    static RouteEntity routeEntity1;
    static RouteEntity routeEntity2;
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
        journeyService = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), JourneyService.class, "transactionalJourneyService");
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
        Long stopid = stationService.createStation(ods);

        kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        stationService.createStation(kyiv);

        routeEntity1 = new RouteEntity("10", ods, kyiv, new Time(20, 27, 0), 29800);
        routeId1 = routeService.save(routeEntity1);

        routeEntity2 = new RouteEntity("10", ods, kyiv, new Time(20, 27, 0), 29800);
        routeId1 = routeService.save(routeEntity1);
    }

    @BeforeEach
    void start() {

    }

    @AfterAll
    static void tearDown() {
        applicationContext.close();
    }

    @Test
    void findOrCreateTrip() {
        LocalDate date1 = LocalDate.now().plusMonths(1);
        LocalDate date2 = LocalDate.now().plusWeeks(2);
        TripEntity trip1 = new TripEntity(routeEntity1, vehicle1, date1);
        tripService.create(trip1);
        TripEntity trip2 = new TripEntity(routeEntity1, vehicle1, date2);
        tripService.create(trip2);


        long tripId1 = tripService.getByRouteDate(routeEntity1.getId(), date1);
        long tripId2 = tripService.getByRouteDate(routeEntity1.getId(), date2);

        TripEntity foundTrip1 = tripService.getById(tripId1);
        System.out.println("---------------------------" + foundTrip1);
        TripEntity foundtrip2 = tripService.getById(tripId2);
        System.out.println("---------------------------" + foundtrip2);
        assertNotEquals(foundTrip1, foundtrip2);
    }

    @Test
    void constrainsTest() {
        LocalDate date = LocalDate.now().plusWeeks(3);
        TripEntity trip1 = new TripEntity(routeEntity1, vehicle1, date);
        tripService.create(trip1);
        try {
            TripEntity trip2 = new TripEntity(routeEntity1, vehicle1, date);
            tripService.create(trip2);
            fail();
        } catch (PersistenceException e) {
            System.out.println("Expected PersistenceException " + e.toString());
        }
    }

    @Test
    void getDeparture() {
        RouteEntity routeEntity = new RouteEntity("75", kyiv, ods, new Time(23, 6, 0), 30000);
        Long routeId = routeService.save(routeEntity);
        LocalDate date = LocalDate.now().plusMonths(1);
        TripEntity trip = new TripEntity(routeEntity, vehicle1, date);
        tripService.create(trip);
        Instant ins = trip.getDeparture();
        assertTrue(ins.toString().contains("T23:06:00Z"));
        System.out.println(ins + "------------------------------------------------------");
    }

    @Test
    void sellTicket() {
        RouteEntity routeEntity = new RouteEntity("75", kyiv, ods, new Time(23, 6, 0), 30000);
        Long routeId = routeService.save(routeEntity);

        LocalDate date = LocalDate.now().plusWeeks(1);
        TripEntity trip = new TripEntity(routeEntity, vehicle1, date);
        tripService.create(trip);

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
    void getMaxFreePlaces() {
        RouteEntity route1 = new RouteEntity("75", kyiv, ods, new Time(23, 6, 0), 30000);
        Long routeId1 = routeService.save(route1);
        RouteEntity route2 = new RouteEntity("218", kyiv, ods, new Time(18, 20, 0), 22000);
        Long routeId2 = routeService.save(route2);

        LocalDate date1 = LocalDate.now().plusDays(1);
        LocalDate date2 = LocalDate.now().plusDays(2);
        LocalDate date3 = LocalDate.now().plusDays(3);
        LocalDate date4 = LocalDate.now().plusDays(4);

        List<TripEntity> trips = new ArrayList<>();
        trips.add(new TripEntity(route1, vehicle1, date1));
        trips.add(new TripEntity(route1, vehicle1, date2));
        trips.add(new TripEntity(route1, vehicle1, date3));
        trips.add(new TripEntity(route1, vehicle1, date4));

        trips.add(new TripEntity(route2, vehicle2, date1));
        trips.add(new TripEntity(route2, vehicle2, date2));
        trips.add(new TripEntity(route2, vehicle2, date3));
        trips.add(new TripEntity(route2, vehicle2, date4));

        int sell = 80;
        for (TripEntity trip : trips) {
            tripService.create(trip);
            sell -= 10;
            for (int i = 0; i < sell; i++) {
                tripService.sellTicket(trip.getId());
            }
        }
        List<TripEntity> result = tripService.getMaxFreePlaces(4);
        assertEquals(4, result.size());
        assertTrue(result.get(0).getTickets() == 1000 && result.get(0).getSold() == 40);
        assertTrue(result.get(1).getTickets() == 1000 && result.get(1).getSold() == 50);
        assertTrue(result.get(2).getTickets() == 1000 && result.get(2).getSold() == 60);
        assertTrue(result.get(3).getTickets() == 1000 && result.get(3).getSold() == 70);
        tripService.sellTicket(trips.get(0).getId());
    }

    @Test
    void getMinFreePlaces() throws UnableToRemove {
        RouteEntity route1 = new RouteEntity("75", kyiv, ods, new Time(23, 6, 0), 30000);
        Long routeId1 = routeService.save(route1);
        RouteEntity route2 = new RouteEntity("218", kyiv, ods, new Time(18, 20, 0), 22000);
        Long routeId2 = routeService.save(route2);

        LocalDate date1 = LocalDate.now().plusDays(1);
        LocalDate date2 = LocalDate.now().plusDays(2);
        LocalDate date3 = LocalDate.now().plusDays(3);
        LocalDate date4 = LocalDate.now().plusDays(4);

        List<TripEntity> trips = new ArrayList<>();
        trips.add(new TripEntity(route1, vehicle1, date1));
        trips.add(new TripEntity(route1, vehicle1, date2));
        trips.add(new TripEntity(route1, vehicle1, date3));
        trips.add(new TripEntity(route1, vehicle1, date4));

        trips.add(new TripEntity(route2, vehicle2, date1));
        trips.add(new TripEntity(route2, vehicle2, date2));
        trips.add(new TripEntity(route2, vehicle2, date3));
        trips.add(new TripEntity(route2, vehicle2, date4));

        int sell = 80;
        for (TripEntity trip : trips) {
            tripService.create(trip);
            sell -= 10;
            for (int i = 0; i < sell; i++) {
                tripService.sellTicket(trip.getId());
            }
        }
        List<TripEntity> result = tripService.getMinFreePlaces(4);
        assertEquals(4, result.size());
        assertTrue(result.get(0).getTickets() == 100 && result.get(0).getSold() == 30);
        assertTrue(result.get(1).getTickets() == 100 && result.get(1).getSold() == 20);
        assertTrue(result.get(2).getTickets() == 100 && result.get(2).getSold() == 10);
        assertTrue(result.get(3).getTickets() == 100 && result.get(3).getSold() == 0);
    }

    @AfterEach
    void clear() throws UnableToRemove {
        List<TripEntity> trips = tripService.getSorted(TripEntity_.ACTIVE);
        for (TripEntity trip : trips) {
            tripService.removeById(trip.getId());
        }
    }
}
