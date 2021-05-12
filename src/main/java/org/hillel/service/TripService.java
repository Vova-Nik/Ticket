package org.hillel.service;

import org.hillel.persistence.entity.*;
import org.hillel.persistence.jpa.repository.TripJPARepository;
import org.hillel.persistence.jpa.repository.specification.TripSpecification;
import org.hillel.persistence.jpa.repository.specification.VehicleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service("TripService")
public class TripService extends EntityServiceImplementation<TripEntity, Long> {

    private TripJPARepository tripRepository;
//    @Autowired
//    private RouteRepository routeRepository;

    @Autowired
    public TripService(TripJPARepository repository) {
        super(TripEntity.class, repository);
        this.tripRepository = repository;
    }

    @Transactional
    public TripEntity save(TripEntity trip) {
        if (Objects.isNull(trip) || !trip.isValid())
            throw new IllegalArgumentException("TripService TripEntity.create is not valid");
        TripEntity result;
        try {
            result = tripRepository.save(trip);
        }catch(DataIntegrityViolationException e){
            System.out.println(e.toString());
            throw new IllegalArgumentException("TripService.save such trip already exists");
        }
        return result;
    }

    @Transactional
    public boolean sellTicket(Long id) {
        TripEntity trip = findById(id);
        if (trip.sellTicket()) {
            save(trip);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public int getAvailiblePlaces(Long id) {
        TripEntity trip = findById(id);
        return trip.getAvailible();
    }

    @Transactional(readOnly = true)
    public int getFree(Long id) {
        TripEntity trip = findById(id);
        return trip.getAvailible();
    }

    @Transactional(readOnly = true)
    public List<TripEntity> findByRouteAndDate(final Long routeId, final LocalDate departure) {
        return tripRepository.findByRouteAndDate(routeId, departure);
    }

    @Transactional(readOnly = true)
    public List<TripEntity> findByRouteAndDateActive(final Long routeId, final LocalDate departure) {
        return tripRepository.findByRouteAndDateActive(routeId, departure);
    }

    @Override
    boolean isValid(TripEntity entity) {
        return entity.isValid();
    }




    @Transactional
    public List<TripEntity> getByrootSpec(final Long rootId) {
        if (Objects.isNull(rootId))
            throw new IllegalArgumentException("TripEntity.getByroot insufficient rootId parameter");
        return tripRepository.findAll(TripSpecification.findByRoute(rootId));
    }

    @Transactional
    public List<TripEntity> getByDateSpec(final LocalDate date) {
        if (Objects.isNull(date))
            throw new IllegalArgumentException("TripEntity.getByroot insufficient rootId parameter");
        return tripRepository.findAll(TripSpecification.findByDate(date));
    }

    @Transactional
    public List<TripEntity> getByRootDateSpec(final Long rootId, final LocalDate date) {
        if (Objects.isNull(rootId) || Objects.isNull(date))
            throw new IllegalArgumentException("TripEntity.getByroot insufficient rootId parameter");
        return tripRepository.findAll(TripSpecification.findByRoute(rootId).and(TripSpecification.findByDate(date)));
    }
}
