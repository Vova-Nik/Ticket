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

    @BeforeAll
    public static void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        vehicleService = applicationContext.getBean(VehicleService.class);
        stationService = applicationContext.getBean(org.hillel.service.StationService.class);
    }

    @Test
    void getByName() {
    }

    @Test
    void getAllByNamedQuery() {

        VehicleEntity vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        VehicleEntity vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);
        VehicleEntity vehicle3 = new VehicleEntity("Test bus", VehicleType.BUS);
        vehicleService.save(vehicle3);
        assertTrue(vehicleService.exists(vehicle1.getId()));
        assertTrue(vehicleService.exists(vehicle2.getId()));
        assertTrue(vehicleService.exists(vehicle3.getId()));

        List<VehicleEntity> vehicles;
        vehicles = vehicleService.findAllByNamedQuery();
        assertTrue(vehicles.size() > 0);

        try {
            vehicleService.deleteById(vehicle1.getId());
            vehicleService.deleteById(vehicle2.getId());
            vehicleService.deleteById(vehicle3.getId());
        } catch (UnableToRemove unableToRemove) {
            unableToRemove.printStackTrace();
            fail();
        }


    }

    @Test
    void findAll() {
        VehicleEntity vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        VehicleEntity vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);
        VehicleEntity vehicle3 = new VehicleEntity("Test bus", VehicleType.BUS);
        vehicleService.save(vehicle3);
        assertTrue(vehicleService.exists(vehicle1.getId()));
        assertTrue(vehicleService.exists(vehicle2.getId()));
        assertTrue(vehicleService.exists(vehicle3.getId()));

        Collection<VehicleEntity> vehicles;
        vehicles = vehicleService.findAll();
        assertTrue(vehicles.size() > 0);
    }

    @Test
    public void findAllSQL() {
        VehicleEntity vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        VehicleEntity vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);
        VehicleEntity vehicle3 = new VehicleEntity("Test bus", VehicleType.BUS);
        vehicleService.save(vehicle3);
        assertTrue(vehicleService.exists(vehicle1.getId()));
        assertTrue(vehicleService.exists(vehicle2.getId()));
        assertTrue(vehicleService.exists(vehicle3.getId()));

        Collection<VehicleEntity> vehicles;
        vehicles = vehicleService.findAllSQL();
        assertTrue(vehicles.size() > 0);
    }

    @Test
    public void findAllCriteria() {
        VehicleEntity vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        VehicleEntity vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);
        VehicleEntity vehicle3 = new VehicleEntity("Test bus", VehicleType.BUS);
        vehicleService.save(vehicle3);
        assertTrue(vehicleService.exists(vehicle1.getId()));
        assertTrue(vehicleService.exists(vehicle2.getId()));
        assertTrue(vehicleService.exists(vehicle3.getId()));

        Collection<VehicleEntity> vehicles;
        vehicles = vehicleService.findAllCriteria();
        assertTrue(vehicles.size() > 0);
    }

    @Test
    public void storedProcExecute() {
        //  SELECT prosrc FROM pg_proc WHERE proname = 'find_all';
        //  DROP FUNCTION find_all;
        VehicleEntity vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        VehicleEntity vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);
        VehicleEntity vehicle3 = new VehicleEntity("Test bus", VehicleType.BUS);
        vehicleService.save(vehicle3);
        assertTrue(vehicleService.exists(vehicle1.getId()));
        assertTrue(vehicleService.exists(vehicle2.getId()));
        assertTrue(vehicleService.exists(vehicle3.getId()));

        VehicleEntity vehicle128 = new VehicleEntity("Bus 128", VehicleType.BUS);
        vehicleService.save(vehicle128);
        Collection<VehicleEntity> vehicles;
        vehicles = vehicleService.storedProcExecute();
        assertEquals(4, vehicles.size());
    }

    @Test
    void getSorted() {
        VehicleEntity vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        VehicleEntity vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);
        VehicleEntity vehicle3 = new VehicleEntity("Test bus", VehicleType.BUS);
        vehicleService.save(vehicle3);
        assertTrue(vehicleService.exists(vehicle1.getId()));
        assertTrue(vehicleService.exists(vehicle2.getId()));
        assertTrue(vehicleService.exists(vehicle3.getId()));

        VehicleEntity veh;
        for (int i = 0; i < 12; i++) {
            int prefix = (int) (Math.random() * 100);
            veh = new VehicleEntity("Vehicle" + prefix + '_' + i, VehicleType.TRAIN);
            vehicleService.save(veh);
        }

        List<VehicleEntity> vehicles = vehicleService.getSorted(VehicleEntity_.NAME);
        assertTrue(vehicles.size() > 8);
        int aa = vehicles.get(0).getName().compareTo(vehicles.get(8).getName());
        assertTrue(aa < 0);

        print(vehicles);
        vehicles = vehicleService.getSorted(StationEntity_.ID);
        assertTrue(vehicles.size() > 8);
        assertTrue(vehicles.get(0).getId() < vehicles.get(7).getId());
        print(vehicles);

        try {
            for (VehicleEntity vehicle : vehicles
            ) {
                vehicleService.deleteById(vehicle.getId());
            }
        } catch (UnableToRemove unableToRemove) {
            unableToRemove.printStackTrace();
            fail();
        }
    }

    @Test
    void getSortedByPage() {
        VehicleEntity vehicle1 = new VehicleEntity("Chernomoretc", VehicleType.TRAIN);
        vehicleService.save(vehicle1);
        VehicleEntity vehicle2 = new VehicleEntity("Green train", VehicleType.TRAIN);
        vehicleService.save(vehicle2);
        VehicleEntity vehicle3 = new VehicleEntity("Test bus", VehicleType.BUS);
        vehicleService.save(vehicle3);
        assertTrue(vehicleService.exists(vehicle1.getId()));
        assertTrue(vehicleService.exists(vehicle2.getId()));
        assertTrue(vehicleService.exists(vehicle3.getId()));

        VehicleEntity veh;
        for (int i = 0; i < 12; i++) {
            int prefix = (int) (Math.random() * 100);
            veh = new VehicleEntity("Vehicle" + prefix + '_' + i, VehicleType.TRAIN);
            vehicleService.save(veh);
        }

        List<VehicleEntity> vehicles = vehicleService.getSortedByPage(5, 1, VehicleEntity_.NAME);
        assertEquals(5, vehicles.size());
        assertEquals("Green train", vehicles.get(0).getName());
        print(vehicles);

        vehicles = vehicleService.getSortedByPage(32, 0, VehicleEntity_.NAME);
        assertEquals(15, vehicles.size());

        try {
            for (VehicleEntity vehicle : vehicles
            ) {
                vehicleService.deleteById(vehicle.getId());
            }
        } catch (UnableToRemove unableToRemove) {
            unableToRemove.printStackTrace();
            fail();
        }
    }

    void print(List<VehicleEntity> vehicles) {
        System.out.println("------------------------------------------------------------");
        for (VehicleEntity vehicle : vehicles
        ) {
            System.out.println(vehicles);
        }
        System.out.println("------------------------------------------------------------");
    }

    @AfterAll
    public static void unSetUp() {
        applicationContext.close();
    }
}