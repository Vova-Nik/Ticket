package org.hillel.service;


import org.hillel.persistence.entity.JourneyEntity;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

public interface JourneyService {
    Collection<JourneyEntity> find(String stationFrom, String stationTo, LocalDate dateFrom, LocalDate dateTo) throws SQLException;
}
