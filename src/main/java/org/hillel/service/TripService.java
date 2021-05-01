package org.hillel.service;

import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.*;
import org.hillel.persistence.repository.RouteRepository;
import org.hillel.persistence.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private RouteRepository routeRepository;

    @Transactional
    public boolean sellTicket(Long id) {
        return tripRepository.sellTicket(id);
    }

    @Transactional(readOnly = true)
    public int getAvailiblePlaces(Long id) {
        return tripRepository.getFreePlaces(id);
    }

    @Transactional(readOnly = true)
    public Long getByRouteDate(Long routeId, LocalDate departure) {
        Optional<Long> id = tripRepository.findByRouteDate(routeId, departure);
        return id.orElseThrow(() -> new IllegalArgumentException("TripService.findOrCreateTrip bad route ID"));
    }

    @Transactional(readOnly = true)
    public TripEntity getById(Long id) {
        return tripRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("TripEntity getById bad ID"));
    }

    @Transactional
    public Long create(TripEntity trip) {
        if (Objects.isNull(trip) || !trip.isValid())
            throw new IllegalArgumentException("TripService TripEntity.create is not valid");
        return tripRepository.createOrUpdate(trip).getId();
    }

    @Transactional(readOnly = true)
    public int getFree(Long id) {
        if (Objects.isNull(id)) throw new IllegalArgumentException("TripService TripEntity.create is not valid");
        TripEntity trip = getById(id);
        return trip.getTickets() - trip.getSold();
    }

    @Transactional(readOnly = true)
    public List<TripEntity> getSortedByPage(int pageSize, int first, String sortBy){
        if (!checkSortingCriteria(sortBy))
            throw new IllegalArgumentException("StationService.getSorted insufficient sortBy parameter");
        return tripRepository.getSortedByPage(pageSize, first, sortBy, true).orElseGet(ArrayList::new);
    }

    @Transactional(readOnly = true)
    public List<TripEntity> getSorted(String sortBy){
        if (!checkSortingCriteria(sortBy))
            throw new IllegalArgumentException("StationService.getSorted insufficient sortBy parameter");
        return tripRepository.getSorted(sortBy, true).orElseGet(ArrayList::new);
    }

    @Transactional(readOnly = true)
    public List<TripEntity> getMaxFreePlaces(int amount) {
        if(amount<1 || amount>1000) throw new IllegalArgumentException("TripService.getMinFreePlaces amount not valid");
        return tripRepository.getFreePlaces(amount, false).orElseGet(ArrayList::new);
    }

    @Transactional(readOnly = true)
    public List<TripEntity> getMinFreePlaces(int amount) {
        if(amount<1 || amount>1000) throw new IllegalArgumentException("TripService.getMinFreePlaces amount not valid");
        return tripRepository.getFreePlaces(amount, true).orElseGet(ArrayList::new);
    }

    @Transactional
    public void removeById(Long id) throws UnableToRemove {
        if(Objects.isNull(id)) return;
        tripRepository.removeById(id);
    }

    boolean checkSortingCriteria(String sortBy) {
        return (sortBy.equals(TripEntity_.ID)
                || sortBy.equals(TripEntity_.NAME)
                || sortBy.equals(TripEntity_.ACTIVE)
                || sortBy.equals(TripEntity_.CREATION_DATE)
                || sortBy.equals(TripEntity_.SOLD)
        );
    }

}
