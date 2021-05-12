package org.hillel.persistence.jpa.repository.specification;

import org.hibernate.query.criteria.internal.OrderImpl;
import org.hillel.persistence.entity.TripEntity;
import org.hillel.persistence.entity.TripEntity_;
import org.hillel.persistence.entity.VehicleEntity;
import org.hillel.persistence.entity.VehicleEntity_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public class TripSpecification {

    public static Specification<TripEntity> findByRoute(Long routeId) {
        return (root, query, criteriaBuilder) -> {
            root.join(TripEntity_.route);
            return criteriaBuilder.equal(root.get(TripEntity_.route), routeId);
        };
    }

    public static Specification<TripEntity> findByDate(final LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(TripEntity_.DEPARTURE_DATE), date);
    }

    public static Specification<TripEntity> findByName(final String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(TripEntity_.NAME), name);
    }
}
