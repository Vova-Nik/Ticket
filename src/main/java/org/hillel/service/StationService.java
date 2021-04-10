package org.hillel.service;

import org.hillel.exceptions.OveralException;
import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service("StationService")
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    @Transactional
    public Long createStation(final StationEntity entity) {
        if (entity == null || !entity.isValid())
            throw new IllegalArgumentException("StopService.createStop - StationEntity is not valid");
        StationEntity newStation = stationRepository.createOrUpdate(entity);
        return newStation.getId();
    }

    @Transactional
    public StationEntity getByName(final String stationName) {
        return stationRepository.getByName(stationName);
    }

    @Transactional
    public StationEntity getById(final Long id) throws OveralException {
        if(id==null||id<0) throw new IllegalArgumentException("StationService.create -  id is not valid");
        return stationRepository.findById(id).orElseThrow(()->new IllegalArgumentException("There is no station having id=" +id));
    }

}
