package org.hillel;

import org.hillel.config.RootConfig;
import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.repository.JourneyRepository;
import org.hillel.service.TicketClient;
import org.hillel.service.TransactionalJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

import java.sql.SQLException;

public class Starter {
    public static void main(String[] args) throws SQLException {
        //       final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("common-beans.xml");
        final ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();
        TicketClient ticketClient = applicationContext.getBean(TicketClient.class);

        JourneyEntity journeyEntity0 = new JourneyEntity();
        journeyEntity0.setStationFrom("Odessa");
        journeyEntity0.setStationTo("Kyiv");
        Long id = ticketClient.createJourney(journeyEntity0);
        System.out.println("Created journeyEntity with id = " + id);

        JourneyEntity journeyEntity1 = new JourneyEntity();
        journeyEntity1.setStationFrom("Odessa");
        journeyEntity1.setStationTo("Lviv");
        id = ticketClient.createJourney(journeyEntity1);
        System.out.println("Created journeyEntity with id = " + id);

        applicationContext.close();
    }

}
