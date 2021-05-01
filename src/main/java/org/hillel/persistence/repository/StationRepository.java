package org.hillel.persistence.repository;

import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.*;
import org.springframework.stereotype.Repository;
import java.util.Objects;

@Repository
public class StationRepository extends ComonRepository<StationEntity, Long> {
    protected StationRepository() {
        super(StationEntity.class);
    }

    @Override
    public void removeById(Long id) throws UnableToRemove {
        if (Objects.isNull(id)) throw new IllegalArgumentException("StationRepository.removeById id is null");
        StationEntity station = findById(id).orElseThrow(() -> new UnableToRemove("Id not exists"));
        entityManager.remove(entityManager.getReference(StationEntity.class, id));
    }

    public void addRoute(final StationEntity station, final RouteEntity route) {
        if (Objects.isNull(station) || Objects.isNull(route))
            throw new IllegalArgumentException("StationRepository.addRoute bad input data");
        StationEntity st = findById(station.getId()).orElseThrow(() -> new IllegalArgumentException("StationRepository.addRoute can not find route"));
        st.addRoute(route);
    }

    public void removeRoute(final StationEntity station, final RouteEntity route) {
        if (Objects.isNull(station) || Objects.isNull(route))
            throw new IllegalArgumentException("StationRepository.addRoute bad input data");
        StationEntity st = findById(station.getId()).orElseThrow(() -> new IllegalArgumentException("StationRepository.addRoute can not find route"));
        st.removeRoute(route);
    }
}
