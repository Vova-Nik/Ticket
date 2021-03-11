package org.hillel.service;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.hillel.model.Journey;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcJorneyServiceImpTest {

    @Test
    void find(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("common-beans.xml");
        Collection<Journey> journeys = new ArrayList<Journey>();
        JourneyService js = (JourneyService) applicationContext.getBean("JorneyService","org.hillel.service.JdbcJorneyServiceImp");
        try {
            journeys = js.find("odessa", "kiev", LocalDate.now(), LocalDate.now().plusDays(2));
        }catch (java.sql.SQLException e){
            fail(e.getMessage());
        }
        System.out.println(journeys);
    }
}