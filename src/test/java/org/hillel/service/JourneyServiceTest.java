package org.hillel.service;

import org.hillel.model.Journey;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class JourneyServiceTest {

    @Test
    void find() throws SQLException {
        JourneyService journeyService = new InMemoryJorneyServiceImp();
        Collection<Journey> journes = journeyService.find("odessa", "kiev", LocalDate.now(), LocalDate.now().plusDays(1));
        assertTrue(journes.size()>0);
        System.out.println(journes);
        journes = journeyService.find("odessa", "abcdefghjklmnosss", LocalDate.now(), LocalDate.now().plusDays(1));
        assertEquals(0,journes.size());
    }
}