package org.hillel.config;

import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan({"org.hillel"})
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource({"classpath:application.properties","classpath:database.properties"})
//@PropertySource({"classpath:application.properties","classpath:testdatabase.properties"})
public class RootConfig {

}
