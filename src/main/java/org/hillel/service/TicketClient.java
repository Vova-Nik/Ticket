package org.hillel.service;

import org.hillel.persistence.entity.JourneyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class TicketClient {

    @Autowired
    private JourneyService journeyService;

    public TicketClient() {
    }

    public long createJourney(final JourneyEntity journeyEntity) {
        return journeyService.save(journeyEntity).getId();
    }

    public Collection<JourneyEntity> find(String stationFrom) {
        return null;
    }
}


