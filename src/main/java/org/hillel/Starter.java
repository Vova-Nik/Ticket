package org.hillel;

import org.hillel.config.RootConfig;
import org.hillel.service.StationService;
import org.hillel.service.TicketClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import java.sql.SQLException;
import java.util.Scanner;

public class Starter {
    public static void main(String[] args) throws SQLException {
        //       final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("common-beans.xml");
        final ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        Environment env = applicationContext.getEnvironment();

        try {
            TablesCreator tablesCreator = applicationContext.getBean("TablesCreator", TablesCreator.class);
            tablesCreator.createStationsPool();
            tablesCreator.createVehiclesPool();
            tablesCreator.createRoutesPool();

            StationService stationService = applicationContext.getBean("StopService", StationService.class);

//            VehicleService vehicleService = applicationContext.getBean("VehicleService", VehicleService.class);
//            vehicleService.createVehicleTestPool();


            TicketClient ticketClient = applicationContext.getBean(TicketClient.class);
                //        JourneyEntity journeyEntity0 = new JourneyEntity();
                //        journeyEntity0.setStationFrom("Odessa");
                //        journeyEntity0.setStationTo("Kyiv");
                //        Long journid = ticketClient.createJourney(journeyEntity0);
                //        System.out.println("Created journeyEntity with id = " + journid);
                //
                //        JourneyEntity journeyEntity1 = new JourneyEntity();
                //        journeyEntity1.setStationFrom("Odessa");
                //        journid = ticketClient.createJourney(journeyEntity1);
                //        System.out.println("Created journeyEntity with id = " + journid);

            Scanner console = new Scanner(System.in);
            System.out.println("Check database tables if necessary, then input any string to finish process");
            console.nextLine();
            console.close();
        } finally {
            applicationContext.close();
        }


    }

}
