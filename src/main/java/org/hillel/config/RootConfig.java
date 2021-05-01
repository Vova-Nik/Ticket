package org.hillel.config;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan({"org.hillel"})
@PropertySource({"classpath:application.properties","classpath:database.properties"})
//@PropertySource({"classpath:application.properties","classpath:testdatabase.properties"})
public class RootConfig {

}
