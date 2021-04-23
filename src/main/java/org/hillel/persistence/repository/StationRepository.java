package org.hillel.persistence.repository;
import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.RouteEntity;
import org.hillel.persistence.entity.StationEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Objects;

@Repository
public class StationRepository extends ComonRepository<StationEntity, Long> {
    protected StationRepository() {
        super(StationEntity.class);
    }

    public StationEntity getByName(String name) {
        String sql = "SELECT * FROM stations where name = ?";
        Query query = entityManager.createNativeQuery(sql, StationEntity.class);
        query.setParameter(1, name);
        StationEntity stationEntity = (StationEntity) query.getSingleResult();
        return stationEntity;
    }

    @Override
    public void removeById(Long id) throws UnableToRemove {
        if(Objects.isNull(id)) throw new IllegalArgumentException("StationRepository.removeById id is null");
        StationEntity station = findById(id).orElseThrow(() -> new UnableToRemove("Id not exists"));
            //super.removeById(id);
        entityManager.remove(entityManager.getReference(StationEntity.class, id));
    }


    public void addRoute(final StationEntity station, final RouteEntity route){
        if(Objects.isNull(station) || Objects.isNull(route)) throw new IllegalArgumentException("StationRepository.addRoute bad input data");
        StationEntity st = findById(station.getId()).orElseThrow(()->new IllegalArgumentException("StationRepository.addRoute can not find route"));
        st.addRoute(route);
    }

    public void removeRoute(final StationEntity station, final RouteEntity route){
        if(Objects.isNull(station) || Objects.isNull(route)) throw new IllegalArgumentException("StationRepository.addRoute bad input data");
        StationEntity st = findById(station.getId()).orElseThrow(()->new IllegalArgumentException("StationRepository.addRoute can not find route"));
        st.removeRoute(route);
    }
}
