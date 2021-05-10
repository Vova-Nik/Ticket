package org.hillel.service;

import org.hillel.config.RootConfig;
import org.hillel.persistence.entity.*;
import org.hillel.persistence.entity.enums.VehicleType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class VehicleServiceTest {

    static ConfigurableApplicationContext applicationContext;
    static Environment env;
    static VehicleService vehicleService;
    static StationService stationService;

    VehicleEntity vehicle1;
    VehicleEntity vehicle2;
    VehicleEntity vehicle3;
    VehicleEntity vehicle4;

    @BeforeAll
    public static void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        vehicleService = applicationContext.getBean(VehicleService.class);
        stationService = applicationContext.getBean(org.hillel.service.StationService.class);
    }

    @BeforeEach
    public void init() {
        vehicleService = (VehicleService) BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getBeanFactory(), org.hillel.service.VehicleService.class, "vehicleService");
        vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);
        vehicle3 = new VehicleEntity("Test bus", VehicleType.BUS);
        vehicleService.save(vehicle3);
        vehicle4 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle4);
        assertTrue(vehicleService.exists(vehicle1.getId()));
        assertTrue(vehicleService.exists(vehicle2.getId()));
        assertTrue(vehicleService.exists(vehicle3.getId()));
        assertTrue(vehicleService.exists(vehicle4.getId()));
    }

    @AfterEach
    void clear() {
        vehicleService.deleteAll();
    }

    @Test
    void comonProc() {

        //find all
        List<VehicleEntity> vehicles = vehicleService.findAll();
        assertEquals(4, vehicles.size());

        //delete
        vehicleService.deleteById(vehicle4.getId());
        vehicles = vehicleService.findAll();
        assertEquals(3, vehicles.size());
        vehicle4 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle4);

        //exists
        assertTrue(vehicleService.exists(vehicle4.getId()));
        vehicleService.deleteById(vehicle4.getId());
        assertFalse(vehicleService.exists(vehicle4.getId()));
        vehicle4.setId(null);
        vehicleService.save(vehicle4);
        assertTrue(vehicleService.exists(vehicle4.getId()));

        //findByIds
        vehicles = vehicleService.findByIds(vehicle1.getId(), vehicle2.getId());
        assertEquals(2, vehicles.size());

        //findByName
        assertEquals(2, vehicleService.findByName("Chernomoretc").size());
        assertEquals(1, vehicleService.findByName("Test bus").size());
    }

    @Test
    void findsTest() {
        //findAll
        List<VehicleEntity> vehicles = vehicleService.findAll();
        assertEquals(4, vehicles.size());

        //findAllCtive
        vehicles = vehicleService.findAllCtive();
        assertEquals(4, vehicles.size());
        vehicle4.setActive(false);
        vehicleService.save(vehicle4);
        vehicles = vehicleService.findAllCtive();
        assertEquals(3, vehicles.size());
        vehicle4.setActive(true);
        vehicleService.save(vehicle4);

        //findByNameActive
        assertEquals(2, vehicleService.findByNameActive("Chernomoretc").size());
        vehicle4.setActive(false);
        vehicleService.save(vehicle4);
        assertEquals(1, vehicleService.findByNameActive("Chernomoretc").size());
    }

    @Test
    void enableDisableTest() {
        List<VehicleEntity> vehicles = vehicleService.findAllCtive();
        assertEquals(4, vehicles.size());
        vehicleService.disableById(vehicle4.getId());
        vehicles = vehicleService.findAllCtive();
        assertEquals(3, vehicles.size());
        vehicleService.enableById(vehicle4.getId());
        vehicles = vehicleService.findAllCtive();
        assertEquals(4, vehicles.size());
    }

    @Test
    void counts(){
        assertEquals(4,vehicleService.count());
        assertEquals(2,vehicleService.countByNameActive(vehicle4.getName()));
        vehicleService.disableById(vehicle4.getId());
        assertEquals(1,vehicleService.countByNameActive(vehicle4.getName()));
    }

    @Test
    void getByNameActiveSpecification(){
        List<VehicleEntity> vehicles = vehicleService.getByNameActiveSpecification(vehicle1.getName());
        assertEquals(2, vehicles.size());
        assertEquals(vehicle1.getName(), vehicles.get(0).getName());
        assertEquals(vehicle1.getName(), vehicles.get(1).getName());
        vehicleService.disableById(vehicle1.getId());
        vehicles = vehicleService.getByNameActiveSpecification(vehicle1.getName());
        assertEquals(1, vehicles.size());

    }

    @Test
    void getByNameOrderedSpecification(){
        VehicleEntity vehicle11;
        List<VehicleEntity> vehicles = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            vehicle11 =new VehicleEntity("Vehicle" + (32-i), VehicleType.TRAIN);
            vehicles.add(vehicle11);
        }
        vehicleService.saveList(vehicles);
        assertEquals(16,vehicleService.count());

        vehicles = vehicleService.getByNameOrderedSpecification(VehicleEntity_.NAME);
        for (int i = 0; i < 14; i++) {
            assertTrue(vehicles.get(i).getName().compareTo(vehicles.get(i+1).getName())<=0);
        }
    }

    @AfterAll
    public static void unSetUp() {
        applicationContext.close();
    }
}