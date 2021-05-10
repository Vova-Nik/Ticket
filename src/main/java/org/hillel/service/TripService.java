package org.hillel.service;

import org.hillel.persistence.entity.*;
import org.hillel.persistence.jpa.repository.TripJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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


/*    @Transactional(readOnly = true)
    public List<TripEntity> getMaxFreePlaces(int amount) {
        if(amount<1 || amount>1000) throw new IllegalArgumentException("TripService.getMinFreePlaces amount not valid");
        return tripRepository.getFreePlaces(amount, false).orElseGet(ArrayList::new);
    }*/

/*    @Transactional(readOnly = true)
    public List<TripEntity> getMinFreePlaces(int amount) {
        if(amount<1 || amount>1000) throw new IllegalArgumentException("TripService.getMinFreePlaces amount not valid");
        return tripRepository.getFreePlaces(amount, true).orElseGet(ArrayList::new);
    }*/


    @Override
    boolean isValid(TripEntity entity) {
        return entity.isValid();
    }
}
