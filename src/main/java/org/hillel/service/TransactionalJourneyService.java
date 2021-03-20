package org.hillel.service;


import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service

public class TransactionalJourneyService{

    @Autowired
    private JourneyRepository journeyRepository;

    @Transactional
    public long createJourney(final JourneyEntity entity){
//        if(journeyEntity==null||!journeyEntity.isValid()) throw new IllegalArgumentException("JourneyEntity is not valid");
        long id = journeyRepository.create(entity);
        return id;
    }

//    @Autowired
//    public void setJourneyRepository(JourneyRepository journeyRepository) {
//        System.out.println("TransactionalJourneyService setter " +  journeyRepository);
//        this.journeyRepository = journeyRepository;
//    }





}
