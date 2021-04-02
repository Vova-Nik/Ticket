package org.hillel.service;

import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("StopService")
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    @Transactional
    public Long createStop(final StationEntity entity){
        if(entity==null||!entity.isValid()) throw new IllegalArgumentException("StopService.createStop - StopEntity is not valid");
        Long id = stationRepository.create(entity);
        return id;
    }

    @Transactional
    public StationEntity getByName(final String stationName){

        StationEntity se= stationRepository.getByName(stationName);
        return se;
    }

}
