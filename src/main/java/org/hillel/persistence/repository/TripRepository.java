package org.hillel.persistence.repository;

import org.hibernate.query.criteria.internal.OrderImpl;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.TripEntity;
import org.hillel.persistence.entity.TripEntity_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TripRepository extends ComonRepository<TripEntity, Long> {

    @Autowired
    RouteRepository routeRepository;

    protected TripRepository() {
        super(TripEntity.class);
    }

    public boolean sellTicket(Long id) {
        TripEntity tripEntity = entityManager.find(TripEntity.class, id);
        return tripEntity.sellTicket();
    }

    public int getFreePlaces(Long id) {
        TripEntity tripEntity = entityManager.find(TripEntity.class, id);
        return tripEntity.getAvailible();
    }

    public Optional<Long> findByRouteDate(final Long routeId, final LocalDate departure) {
        Class<TripEntity> tripClass = TripEntity.class;
        RouteEntity route = routeRepository.findById(routeId).orElseThrow(() -> new IllegalArgumentException("TripRepository.findByRouteDate bad route ID"));

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<TripEntity> query = criteriaBuilder.createQuery(tripClass);
        final Root<TripEntity> from = query.from(tripClass);

        final Predicate byRoute = criteriaBuilder.equal(from.get(TripEntity_.ROUTE), criteriaBuilder.parameter(RouteEntity.class, "routeParam"));
        final Predicate byDate = criteriaBuilder.equal(from.get(TripEntity_.DEPARTURE_DATE), criteriaBuilder.parameter(LocalDate.class, "departureParam"));

        List<TripEntity> trips = entityManager.createQuery(query.select(from)
                .where(byRoute, byDate))
                .setParameter("routeParam", route)
                .setParameter("departureParam", departure)
                .getResultList();

        if (trips.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(trips.get(0).getId());
    }


    public Optional<List<TripEntity>> getFreePlaces(int pageSise, boolean ascending) {
        if (pageSise < 1 || pageSise > 1000)
            throw new IllegalArgumentException("StationRepository.getSorted insufficient pageination parameters");

        String query;
        if(ascending)
            query= "SELECT t FROM TripEntity t order by ABS(t." +TripEntity_.TICKETS + " - t."+ TripEntity_.SOLD + ") asc";
        else
            query= "SELECT t FROM TripEntity t order by ABS(t." +TripEntity_.TICKETS + " - t."+ TripEntity_.SOLD + ")  desc";

        TypedQuery<TripEntity> typedQuery = entityManager.createQuery(query, TripEntity.class);

        List<TripEntity> trips = typedQuery
                .setFirstResult(0)
                .setMaxResults(pageSise)
                .getResultList();

        if (trips.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(trips);
    }

}
