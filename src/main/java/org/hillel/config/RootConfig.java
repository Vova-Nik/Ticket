package org.hillel.config;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan({"org.hillel"})
@PropertySource({"classpath:application.properties","classpath:database.properties"})
public class RootConfig {

//    @Bean("TransactionalJourneyService")
//    public TransactionalJourneyService getTransactionalJourneyService(){
//        return new TransactionalJourneyService();
//    }

//    @Bean("TicketClient")
//    public TicketClient getTicketClient(){
//////        return new TicketClient(getTransactionalJourneyService());
//        return new TicketClient();
//    }

//    @Autowired
//    private org.springframework.jdbc.datasource.DriverManagerDataSource dataSource;
//    @Autowired
//    private DBCPDataAccess dataAccess;
//    @Autowired
//    private RouteRepository routeRepository;
//    @Autowired
//    private StationRepository stationRepository;
//    @Autowired
//    private JourneyService jorneyService;
//
//    @Bean("dataSource")
//    @Order(0)
//    public org.springframework.jdbc.datasource.DriverManagerDataSource getDataSource(){
//        return new org.springframework.jdbc.datasource.DriverManagerDataSource(
//                "jdbc:postgresql://94.158.155.216:5432/ticket",
//                "Vova",
//                "641947"
//        );
//    }
//    @Bean("databasePopulator")
//    @DependsOn("dataSource")
//    DatabasePopulator getDatabasePopulator(){
//        return new DatabasePopulator(dataSource);
//    }
//
//    @Bean("dataAccess")
//    @DependsOn("dataSource")
//    public DBCPDataAcessImpl getDBCPDataAcessImpl() throws SQLException {
//        return new DBCPDataAcessImpl(dataSource);
//    }

//    @Bean("routeRepository")
//    @DependsOn("dataAccess")
//    @Order(0)
//    public RouteRepositoryTrainImpl getRouteRepositoryTrainImpl(){
//        return new RouteRepositoryTrainImpl(dataAccess);
//    }
//
//    @Bean("stationRepository")
//    @DependsOn("dataAccess")
//    @Order(0)
//    public StationRepositoryImpl getStationRepositoryImpl() throws SQLException {
//        return new StationRepositoryImpl(dataAccess);
//    }
//
//    @Bean("jorneyService")
//    @Order(0)
//    @DependsOn({"routeRepository", "stationRepository"})
//    public JourneyService getJdbcJorneyServiceImp(){
//        return new JdbcJorneyServiceImp(routeRepository, stationRepository);
//    }

//    @Bean("ticketClient")
//    @DependsOn("jorneyService")
//    @Order(0)
//    public TicketClient getJTicketClient(){
//        return new TicketClient(jorneyService);
//    }


}
