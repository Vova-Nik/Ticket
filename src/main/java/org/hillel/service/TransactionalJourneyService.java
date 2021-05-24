package org.hillel.service;


import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.*;
import org.hillel.persistence.repository.JourneyRepository;
import org.hillel.persistence.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Service(value = "transactionalJourneyService")
public class TransactionalJourneyService implements JourneyService {


    private final JourneyRepository journeyRepository;

    @Transactional
    @Override
    public Long createJourney(final JourneyEntity entity) {
        if (entity == null || !entity.isValid()) throw new IllegalArgumentException("JourneyEntity is not valid");
        return journeyRepository.createOrUpdate(entity).getId();
    }

    @Autowired
    TransactionalJourneyService(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    @Transactional(readOnly = true)
    public Optional<JourneyEntity> getById(final Long id, boolean withDependences) {
        if (id == null || id < 0)
            throw new IllegalArgumentException("TransactionalJourneyService.create - JourneyService is not valid");
        final Optional<JourneyEntity> byId = journeyRepository.findById(id);
        if (withDependences && byId.isPresent()) {
            final JourneyEntity journeyEntity = byId.get();
            journeyEntity.getStationFrom().getName();
            journeyEntity.getStationTo().getName();
        }
        return byId;
    }

    @Override
    public List<JourneyEntity> find(final String stationFrom, final String stationTo, final LocalDate dateFrom, final LocalDate dateTo) throws SQLException {
        return null;
    }

    @Transactional
    public void deleteById(final Long id) throws UnableToRemove {
        if (Objects.nonNull(id)) {
            journeyRepository.removeById(id);
        }
    }

    @Transactional
    public void delete(final JourneyEntity entity) throws UnableToRemove {
        if (Objects.nonNull(entity)) {
            journeyRepository.remove(entity);
        }
    }

    @Transactional
    public boolean exists(final Long id) {
        return journeyRepository.exists(id);
    }


    @Transactional(readOnly = true)
    public List<JourneyEntity> getSortedByPage(int pageSize, int first, String sortBy) {
        if (!checkSortingCriteria(sortBy))
            throw new IllegalArgumentException("transactionalJourneyService.getSorted insufficient sortBy parameter");
        return journeyRepository.getSortedByPage(pageSize, first, sortBy, true).orElseGet(ArrayList::new);
    }

    @Transactional(readOnly = true)
    public List<JourneyEntity> getSorted(String sortBy) {
        if (!checkSortingCriteria(sortBy))
            throw new IllegalArgumentException("transactionalJourneyService.getSorted insufficient sortBy parameter");
        return journeyRepository.getSorted(sortBy, true).orElseGet(ArrayList::new);
    }

    boolean checkSortingCriteria(String sortBy) {
        return (sortBy.equals(JourneyEntity_.ID) || sortBy.equals(JourneyEntity_.NAME) || sortBy.equals(JourneyEntity_.ACTIVE) || sortBy.equals(JourneyEntity_.CREATION_DATE));
    }

}
