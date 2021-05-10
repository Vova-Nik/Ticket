package org.hillel.service;


import org.hillel.persistence.entity.*;
import org.hillel.persistence.jpa.repository.JourneyJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "journeyService")
public class JourneyService extends EntityServiceImplementation<JourneyEntity,Long>{

    private JourneyJPARepository journeyRepository;

    @Autowired
    public JourneyService(JourneyJPARepository journeyRepository){
        super(JourneyEntity.class, journeyRepository);
        this.journeyRepository = journeyRepository;
    }


//    @Transactional
//    public Long createJourney(final JourneyEntity entity) {
//        if (entity == null || !entity.isValid()) throw new IllegalArgumentException("JourneyEntity is not valid");
//        return journeyRepository.createOrUpdate(entity).getId();
//    }

    @Override
    boolean isValid(JourneyEntity entity) {
        return entity.isValid();
    }


}
