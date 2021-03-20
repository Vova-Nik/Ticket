package org.hillel.service;


import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Service(value="transactionalJourneyService")
public class TransactionalJourneyService implements JourneyService{

    @Autowired
    private JourneyRepository journeyRepository;

    @Transactional
    @Override
    public Long createJourney(final JourneyEntity entity){
       if(entity==null||!entity.isValid()) throw new IllegalArgumentException("JourneyEntity is not valid");
        Long id = journeyRepository.create(entity);
        return id;
    }

    @Override
    public Collection<JourneyEntity> find(String stationFrom, String stationTo, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
        return null;
    }

}
