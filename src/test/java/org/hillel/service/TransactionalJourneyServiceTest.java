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
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionalJourneyServiceTest {

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

    @BeforeEach
    public void setUp() {

        applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        journeyService = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), JourneyService.class, "transactionalJourneyService");
        vehicleService = applicationContext.getBean(VehicleService.class);
        stationService = applicationContext.getBean(org.hillel.service.StationService.class);
        routeService = applicationContext.getBean(RouteService.class);

        ods = new StationEntity("Odessa");
        ods.setStationType(StationType.TRANSIT);
        Long stopid = stationService.createStation(ods);

        kyiv = new StationEntity("Kyiv");
        kyiv.setStationType(StationType.TRANSIT);
        stationService.createStation(kyiv);

        routeEntity1 = new RouteEntity("10", ods, kyiv, new Time(20, 0, 0), 30200);
        routeId1 = routeService.save(routeEntity1);
        routeEntity2 = new RouteEntity("9", kyiv, ods, new Time(19, 6, 0), 30100);
        routeId2 = routeService.save(routeEntity2);
    }


    @Test
    void getById() {
        vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);

        journeyEntity1 = new JourneyEntity(routeEntity1, ods, kyiv, LocalDate.now());
        Long js1 = journeyService.createJourney(journeyEntity1);

        journeyEntity2 = new JourneyEntity(routeEntity2, kyiv, ods, LocalDate.now());
        Long js2 = journeyService.createJourney(journeyEntity2);
    }

/*    @Test
    void removeVehicle() throws UnableToRemove {
        vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);

        journeyEntity1 = new JourneyEntity(routeEntity1, ods, kyiv, LocalDate.now());
        Long je1 = journeyService.createJourney(journeyEntity1);
        journeyEntity2 = new JourneyEntity(routeEntity2, kyiv, ods, LocalDate.now());
        Long je2 = journeyService.createJourney(journeyEntity2);

        long vehId = vehicle1.getId();
        assertTrue(vehicleService.exists(vehId));
        vehicleService.deleteById(vehId);
        assertFalse(vehicleService.exists(vehId));
        assertFalse(journeyService.exists(journeyEntity1.getId()));
        assertTrue(journeyService.exists(journeyEntity2.getId()));
    }*/

    @Test
    void removeJourney() throws UnableToRemove {
        vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);

        journeyEntity1 = new JourneyEntity(routeEntity1, ods, kyiv, LocalDate.now());
        Long js1 = journeyService.createJourney(journeyEntity1);

        journeyEntity2 = new JourneyEntity(routeEntity2, kyiv, ods, LocalDate.now());
        Long js2 = journeyService.createJourney(journeyEntity2);

        VehicleEntity veh1 = vehicle1;

//        long vehId = vehicle1.getId();
//        assertTrue(vehicleService.exists(vehId));
//        long jourId = journeyEntity1.getId();
//        journeyService.deleteById(jourId);
//
//        assertTrue(vehicleService.exists(vehId));
//        assertFalse(journeyService.exists(journeyEntity1.getId()));
//        assertTrue(journeyService.exists(journeyEntity2.getId()));
    }
    @Test
    void getSortedByPage() {

        journeyEntity1 = new JourneyEntity(routeEntity1, ods, kyiv, LocalDate.now());
        Long js1 = journeyService.createJourney(journeyEntity1);

        journeyEntity2 = new JourneyEntity(routeEntity2, kyiv, ods, LocalDate.now());
        Long js2 = journeyService.createJourney(journeyEntity2);

        JourneyEntity jour;

        for (int i = 0; i < 16; i++) {
            jour = new JourneyEntity(routeEntity2, kyiv, ods, LocalDate.now().plusDays(i));
            journeyService.createJourney(jour);
        }

        List<JourneyEntity> journeys = journeyService.getSortedByPage(20,  0, JourneyEntity_.NAME);
        System.out.println("*****************************************************************************");
        for (JourneyEntity journey:journeys
             ) {
            System.out.println(journey);
        }
        System.out.println("*****************************************************************************");


    }


    @AfterEach
    public void unSetUp() {
        applicationContext.close();
    }
}