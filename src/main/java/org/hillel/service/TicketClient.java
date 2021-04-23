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


