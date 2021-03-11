package org.hillel.service;

//import org.hillel.ApplicationContextProvider;
import org.hillel.model.Journey;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class TicketClientTest {

    @Test
    void inMemoryFind() throws SQLException {

        final JourneyService journeyService = new InMemoryJorneyServiceImp();
        final TicketClient ticketClient = new TicketClient(journeyService);

        Collection<Journey> ticket = ticketClient.find("Odessa", "Kiev", LocalDate.now(), LocalDate.now().plusDays(3));
        assertTrue(ticket.size()>0);
        System.out.println(ticket);
        ticket = ticketClient.find("Odessa ", " Kiev", LocalDate.now(), LocalDate.now().plusDays(3));
        assertTrue(ticket.size()>0);
        ticket = ticketClient.find("Odessa", "Dnepr", LocalDate.now(), LocalDate.now().plusDays(1));
        assertEquals(ticket.size(),0);

        try {
            ticket = ticketClient.find("", "Kiev", LocalDate.now(), LocalDate.now().plusDays(1));
            fail("Expected IllegalArgumentException");
        }catch(IllegalArgumentException e){
            assertTrue(e.getMessage().contains("Station from must be set"));
        }
        try {
            ticket = ticketClient.find("Odessa", "Kiev", LocalDate.now(), LocalDate.now().minusDays(4));
            fail("Expected IllegalArgumentException");
        }catch(IllegalArgumentException e){
            assertTrue(e.getMessage().contains("Arrival day is before today"));
        }
    }

    @Test
    void inDBFind() throws SQLException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("common-beans.xml");
        final JourneyService journeyService = (JourneyService) applicationContext.getBean("JorneyService");
        final TicketClient ticketClient = new TicketClient(journeyService);

        Collection<Journey> ticket = ticketClient.find("Odessa", "Kiev", LocalDate.now(), LocalDate.now().plusDays(3));
        assertTrue(ticket.size()>0);
        System.out.println(ticket);
        ticket = ticketClient.find("Odessa ", " Kiev", LocalDate.now(), LocalDate.now().plusDays(3));
        assertTrue(ticket.size()>0);
        ticket = ticketClient.find("Odessa", "Dnepr", LocalDate.now(), LocalDate.now().plusDays(1));
        assertEquals(ticket.size(),0);

        try {
            ticket = ticketClient.find("", "Kiev", LocalDate.now(), LocalDate.now().plusDays(1));
            fail("Expected IllegalArgumentException");
        }catch(IllegalArgumentException e){
            assertTrue(e.getMessage().contains("Station from must be set"));
        }
        try {
            ticket = ticketClient.find("Odessa", "Kiev", LocalDate.now(), LocalDate.now().minusDays(4));
            fail("Expected IllegalArgumentException");
        }catch(IllegalArgumentException e){
            assertTrue(e.getMessage().contains("Arrival day is before today"));
        }
    }
}