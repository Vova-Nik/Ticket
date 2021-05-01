package org.hillel.service;


import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.entity.VehicleEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface JourneyService {
    Collection<JourneyEntity> find(String stationFrom, String stationTo, LocalDate dateFrom, LocalDate dateTo) throws SQLException;

    Long createJourney(JourneyEntity journeyEntity);

    void deleteById(Long id) throws UnableToRemove;

    void delete(JourneyEntity entity) throws UnableToRemove;

    boolean exists(Long id);

    List<JourneyEntity> getSortedByPage(int pageSize, int first, String sortBy);

    public List<JourneyEntity> getSorted(String sortBy);
}