package org.hillel.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
//@PropertySource("classpath:database.properties")
//@PropertySource("classpath:testdatabase.properties")
@EnableTransactionManagement
public class DatabaseConfig {
    @Autowired
    Environment environment;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(PGSimpleDataSource.class.getName());
        config.setUsername(environment.getProperty("postgres.username"));
        config.setPassword(environment.getProperty("postgres.password"));
        config.addDataSourceProperty("databaseName", environment.getProperty("postgres.databaseName"));
        config.addDataSourceProperty("serverName", environment.getProperty("postgres.serverName"));

        config.setMaximumPoolSize(150);
        config.setMinimumIdle(30);

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean emf(DataSource dataSource){
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("org.hillel.persistence.entity");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.put("hibernate.dialect", PostgreSQL10Dialect.class.getName());
        properties.put("hibernate.hbm2ddl.auto","create-drop"); //create create-drop update validate
        properties.put("hibernate.show_sql","true");
        emf.setJpaProperties(properties);
        return emf;
    }

    @Bean
    PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        jpaTransactionManager.setDataSource(dataSource());

        jpaTransactionManager.setDefaultTimeout(50);

        return jpaTransactionManager;
    }
}
