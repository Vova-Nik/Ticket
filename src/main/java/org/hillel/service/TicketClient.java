package org.hillel.service;

import org.hillel.persistence.entity.JourneyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class TicketClient {

    @Autowired
    @Qualifier("transactionalJourneyService")
    private JourneyService transactionalJourneyService;

    public TicketClient() {
    }

    public long createJourney(final JourneyEntity journeyEntity) {
        long tjs = transactionalJourneyService.createJourney(journeyEntity);
        return tjs;
    }

    public Collection<JourneyEntity> find(String stationFrom) {
        return null;
    }
}

//    public Collection<JourneyEntity> find(String stationFrom, String stationTo, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
//        if (!StringUtils.hasText(stationFrom)) throw new IllegalArgumentException("Station from must be set");
//        stationFrom = (StringUtils.trimAllWhitespace(stationFrom)).toLowerCase();
//        if (!StringUtils.hasText(stationTo)) throw new IllegalArgumentException("Station to must be set");
//        stationTo = StringUtils.trimAllWhitespace(stationTo).toLowerCase();
//        if (dateFrom.isBefore(LocalDate.now())) throw new IllegalArgumentException("Departure day is before today");
//        if (dateTo.isBefore(LocalDate.now())) throw new IllegalArgumentException("Arrival day is before today");
//        if (dateTo.isBefore(dateFrom)) throw new IllegalArgumentException("Arrival day is before departure day");
//        return journeyService.find(stationFrom, stationTo, dateFrom, dateTo);
//    }

