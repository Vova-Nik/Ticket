package org.hillel.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void getDatedDeparture() {
        Route route = new Route(1,"aaa","bbb","dayly", LocalTime.of(18,30),520,"test");
        LocalDateTime ldt = route.getDatedDeparture(LocalDate.of(2021,3,10));
        System.out.println("Departure - " + ldt);
        assertEquals("2021-03-10T18:30",ldt.toString());
    }

    @Test
    void getDatedArrival() {
        Route route = new Route(1,"aaa","bbb","dayly", LocalTime.of(18,30),520,"test");
        LocalDateTime ldt = route.getDatedArrival(LocalDate.of(2021,3,10));
        System.out.println("Arrival - " + ldt);
        assertEquals("2021-03-11T03:10",ldt.toString());
    }
}
