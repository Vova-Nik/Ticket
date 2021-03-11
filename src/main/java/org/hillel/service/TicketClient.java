package org.hillel.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hillel.model.Journey;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@AllArgsConstructor
public class TicketClient {
    private JourneyService journeyService;

    public Collection<Journey> find(String stationFrom, String stationTo, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
        if (!StringUtils.hasText(stationFrom)) throw new IllegalArgumentException("Station from must be set");
        stationFrom = (StringUtils.trimAllWhitespace(stationFrom)).toLowerCase();
        if (!StringUtils.hasText(stationTo)) throw new IllegalArgumentException("Station to must be set");
        stationTo = StringUtils.trimAllWhitespace(stationTo).toLowerCase();
        if (dateFrom.isBefore(LocalDate.now())) throw new IllegalArgumentException("Departure day is before today");
        if (dateTo.isBefore(LocalDate.now())) throw new IllegalArgumentException("Arrival day is before today");
        if (dateTo.isBefore(dateFrom)) throw new IllegalArgumentException("Arrival day is before departure day");

        return journeyService.find(stationFrom, stationTo, dateFrom, dateTo);
    }
}

