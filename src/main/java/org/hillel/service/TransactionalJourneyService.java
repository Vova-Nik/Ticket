package org.hillel.service;


import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.JourneyEntity;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.VehicleEntity;
import org.hillel.persistence.repository.JourneyRepository;
import org.hillel.persistence.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service(value = "transactionalJourneyService")
public class TransactionalJourneyService implements JourneyService {

    @Autowired
    private JourneyRepository journeyRepository;
    @Autowired
    private VehicleService vehicleService;

    @Transactional
    @Override
    public Long createJourney(final JourneyEntity entity) {
        if (entity == null || !entity.isValid()) throw new IllegalArgumentException("JourneyEntity is not valid");
        return journeyRepository.createOrUpdate(entity).getId();
    }

    TransactionalJourneyService() {
    }

    @Transactional(readOnly = true)
    public Optional<JourneyEntity> getById(final Long id, boolean withDependences) {
        if (id == null || id < 0)
            throw new IllegalArgumentException("TransactionalJourneyService.create - JourneyService is not valid");
        final Optional<JourneyEntity> byId = journeyRepository.findById(id);
        if (withDependences && byId.isPresent()) {
            final JourneyEntity journeyEntity = byId.get();
            journeyEntity.getVehicleEntity().getName();
            journeyEntity.getStationFrom().getName();
            journeyEntity.getStationTo().getName();
        }
        return byId;
    }

    @Override
    public Collection<JourneyEntity> find(final String stationFrom, final String stationTo, final LocalDate dateFrom, final LocalDate dateTo) throws SQLException {
        return null;
    }

    @Transactional
    @Override
    public void setVehicle(final JourneyEntity journey, final VehicleEntity vehicleEntity) {
        if (Objects.isNull(journey) || !journey.isValid() || Objects.isNull(vehicleEntity) || !vehicleEntity.isValid())
            throw new IllegalArgumentException("TransactionalJourneyService setVehicle error");
        journey.setVehicle(vehicleEntity);
        journeyRepository.createOrUpdate(journey);
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
    public boolean exists(final Long id){
        return journeyRepository.exists(id);
    }

}
