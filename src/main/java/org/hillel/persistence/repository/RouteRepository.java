package org.hillel.persistence.repository;

import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.AbstractEntity;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RouteRepository extends ComonRepository<RouteEntity, Long> {
    protected RouteRepository() {
        super(RouteEntity.class);
    }

    public boolean containsStation(final RouteEntity route, final StationEntity station) {
        if (Objects.isNull(station)) return false;
        Optional<RouteEntity> re = findById(route.getId());
        if (!re.isPresent()) return false;
        return re.get().containsStation(station);
    }

    public void addStation(final RouteEntity route, final StationEntity station) {
        if (Objects.isNull(route) || Objects.isNull(station) || Objects.isNull(station.getId()))
            throw new IllegalArgumentException("addStation bad input arguments");
        RouteEntity routeToChange = findById(route.getId()).orElseThrow(() -> new IllegalArgumentException("addStation bad route received from DB"));
        routeToChange.addStation(station);
    }

    public void removeStation(final RouteEntity route, final StationEntity station) throws UnableToRemove {
        if (Objects.isNull(route) || Objects.isNull(station) || Objects.isNull(station.getId()))
            throw new IllegalArgumentException("addStation bad input arguments");
        RouteEntity routeToChange = findById(route.getId()).orElseThrow(() -> new IllegalArgumentException("addStation bad route received from DB"));
        routeToChange.removeStation(station);
    }
}
