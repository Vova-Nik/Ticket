package org.hillel.persistence.jpa.repository.specification;

import org.hibernate.query.criteria.internal.OrderImpl;
import org.hillel.persistence.entity.VehicleEntity;
import org.hillel.persistence.entity.VehicleEntity_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

public class VehicleSpecification {

    public static Specification<VehicleEntity> byName(final String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(VehicleEntity_.NAME), criteriaBuilder.literal(name));
    }

    public static Specification<VehicleEntity> onlyActive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get(VehicleEntity_.ACTIVE));
    }

    public static Specification<VehicleEntity> ordered(final String column) {
        return (root, query, criteriaBuilder) -> {
            final OrderImpl order = new OrderImpl(root.get(column), true);
            query.orderBy(order);
            return criteriaBuilder.and(root.get(VehicleEntity_.ID).isNotNull());
        };
    }

    public static Specification<VehicleEntity> getAllll() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(root.get(VehicleEntity_.NAME).isNotNull());
        };
    }
}
