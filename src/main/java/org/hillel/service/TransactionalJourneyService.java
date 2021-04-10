package org.hillel.service;


import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
               return journeyRepository.createOrUpdate(entity).getId();
     }

    @Transactional
    public JourneyEntity getById(final Long id){
        if(id==null||id<0) throw new IllegalArgumentException("TransactionalJourneyService.create - JourneyService is not valid");
        return journeyRepository.findById(id).orElseThrow(()->new IllegalArgumentException("TransactionalJourneyService.getById - unable to get data by id=" +id));
    }

    @Override
    public Collection<JourneyEntity> find(String stationFrom, String stationTo, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
        return null;
    }

}
