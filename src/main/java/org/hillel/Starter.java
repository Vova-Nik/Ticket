package org.hillel;

import org.hillel.config.RootConfig;
import org.hillel.exceptions.OveralException;
import org.hillel.service.StationService;
import org.hillel.service.TicketClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import java.sql.SQLException;
import java.util.Scanner;

public class Starter {
    public static void main(String[] args) throws SQLException, OveralException {
        final ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();

        try {
            TablesCreator tablesCreator = applicationContext.getBean("TablesCreator", TablesCreator.class);
            tablesCreator.createStationsPool();
            tablesCreator.createVehiclesPool();
            tablesCreator.createRoutesPool();
            tablesCreator.createJourneys();

            TicketClient ticketClient = applicationContext.getBean(TicketClient.class);

            Scanner console = new Scanner(System.in);
            System.out.println("Check database tables if necessary, then input any string in console to finish process");
            console.nextLine();
            console.close();
        } finally {
            applicationContext.close();
        }
    }
}
