package org.hillel.service;

import org.hillel.config.RootConfig;
import org.hillel.persistence.entity.ClientEntity;
import org.hillel.persistence.entity.ClientEntity_;
import org.hillel.persistence.entity.VehicleEntity;
import org.junit.jupiter.api.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import sun.security.acl.AclEntryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    private static ConfigurableApplicationContext applicationContext;
    private static Environment env;
    private static ClientService clientService;

    private List<ClientEntity> clients;

    ClientEntity client1;
    ClientEntity client2;
    ClientEntity client3;
    ClientEntity client4;

    @BeforeAll
    static void init() {
        applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        clientService = applicationContext.getBean(ClientService.class);
    }

    @BeforeEach
    void setUp() {
        client1 = new ClientEntity("Bob");
        client2 = new ClientEntity("Dan");
        client4 = new ClientEntity("Cler");
        client3 = new ClientEntity("Ann");
        client1 = clientService.save(client1);
        client2 = clientService.save(client2);
        client3 = clientService.save(client3);
        client4 = clientService.save(client4);
    }

    @AfterEach
    void tearDown() {
        clientService.deleteAll();
    }

    @AfterAll
    static void close() {
        applicationContext.close();
    }

    @Test
    void saveList() {
        ClientEntity client;
        List<ClientEntity> clients = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            client = new ClientEntity("Person " + i);
            clients.add(client);
        }
        clientService.saveList(clients);
        assertEquals((4 + 12), clientService.count());
    }

    @Test
    void findByIds() {
        List<ClientEntity> clients = clientService.findByIds(client1.getId(), client2.getId(), client4.getId());
        assertEquals(3, clients.size());
        assertTrue(clients.contains(client1));
        assertTrue(clients.contains(client2));
        assertTrue(clients.contains(client4));
        assertFalse(clients.contains(client3));
    }

    @Test
    void findAllActive() {
        List<ClientEntity> clients = clientService.findAllCtive();
        assertEquals(4, clients.size());
        clientService.disableById(client1.getId());
        clients = clientService.findAllCtive();
        assertEquals(3, clients.size());
        clientService.disableById(client4.getId());
        clients = clientService.findAllCtive();
        assertEquals(2, clients.size());
        clientService.enableById(client1.getId());
        clientService.enableById(client4.getId());
        clients = clientService.findAllCtive();
        assertEquals(4, clients.size());
    }

    @Test
    void findByName() {
        List<ClientEntity> clients = clientService.findByName("Cler");
        assertEquals(1, clients.size());
        assertEquals("Cler", clients.get(0).getName());
        ClientEntity client11 = new ClientEntity("Cler");
        clientService.save(client11);
        clients = clientService.findByName("Cler");
        assertEquals(2, clients.size());
        assertEquals("Cler", clients.get(0).getName());
        assertEquals("Cler", clients.get(1).getName());

        clientService.disableById(clients.get(1).getId());
        clients = clientService.findByNameActive("Cler");
        assertEquals(1, clients.size());
    }

    @Test
    void getSortedByPage() {
        ClientEntity client;
        List<ClientEntity> clients = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            client = new ClientEntity("Person_" + (36 - i));
            clients.add(client);
        }
        clientService.saveList(clients);
        clients = clientService.findSortedByPage(0, 4, ClientEntity_.NAME);
        assertEquals(4, clients.size());
        assertEquals("Ann", clients.get(0).getName());
        assertEquals("Dan", clients.get(3).getName());

        clients = clientService.findSortedByPage(1, 15, ClientEntity_.NAME);
        assertEquals(1, clients.size());
        assertEquals("Person_36", clients.get(0).getName());
        try {
            clients = clientService.findSortedByPage(1, 15, "jfhsdlgjsdj;");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("illegal column name"));
        }
    }

    @Test
    void getActiveSortedByPage() {
        ClientEntity client;
        List<ClientEntity> clients = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            client = new ClientEntity("Person_" + (36 - i));
            clients.add(client);
        }
        clientService.saveList(clients);
        clients = clientService.findActiveSortedByPage(0, 4, ClientEntity_.NAME);
        assertEquals(4, clients.size());
        assertEquals("Ann", clients.get(0).getName());
        assertEquals("Dan", clients.get(3).getName());

        clients = clientService.findActiveSortedByPage(1, 15, ClientEntity_.NAME);
        assertEquals(1, clients.size());
        assertEquals("Person_36", clients.get(0).getName());
        try {
            clients = clientService.findActiveSortedByPage(1, 15, "jfhsdlgjsdj;");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("illegal column name"));
        }

        clientService.disableById(client1.getId());
        clients = clientService.findActiveSortedByPage(1, 15, ClientEntity_.NAME);
        assertEquals(0, clients.size());
    }

    @Test
    void findByExample() {

        ClientEntity client11 = new ClientEntity("Bob", "Bobikov");
        ClientEntity client12 = new ClientEntity("Bob", "Kotikov");
        clientService.save(client11);
        clientService.save(client12);

        ClientEntity clientExample = new ClientEntity("Bob");
        clientExample.setSurname(null);
        clientExample.setEmail(null);
        clientExample.setPwd(null);
        clientExample.setId(null);
        clientExample.setCreationDate(null);

        Example<ClientEntity> example = Example.of(clientExample);

        List<ClientEntity> clients = clientService.findAllByExample(example);
        assertEquals(3, clients.size());
        clientService.disableById(client11.getId());
        clients = clientService.findAllByExample(example);
        assertEquals(2, clients.size());

    }
}