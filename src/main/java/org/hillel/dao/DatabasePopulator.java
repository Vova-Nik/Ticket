package org.hillel.dao;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

public class DatabasePopulator {
    private final DriverManagerDataSource dataSource;

    DatabasePopulator(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
        runShemaSQL();
    }

    public DataSource runShemaSQL() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(true);
        databasePopulator.addScript(new ClassPathResource("schema.sql"));
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        return dataSource;
    }
}
