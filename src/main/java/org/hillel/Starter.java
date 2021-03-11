package org.hillel;

import org.hillel.model.Journey;
import org.hillel.service.TicketClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

public class Starter {
    public static void main(String[] args) throws SQLException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("common-beans.xml");
        final TicketClient ticketClient = applicationContext.getBean(TicketClient.class);

        Collection<Journey> tickets = ticketClient.find("Odessa", "Kiev", LocalDate.now(), LocalDate.now().plusDays(3));
        System.out.println(tickets);
        tickets = ticketClient.find("ODESSA", "Lviv", LocalDate.now(), LocalDate.now().plusDays(2));
        System.out.println(tickets);
        tickets = ticketClient.find("ODESSA", "Syktyvkar", LocalDate.now(), LocalDate.now().plusDays(2));
        System.out.println(tickets);
    }
}
