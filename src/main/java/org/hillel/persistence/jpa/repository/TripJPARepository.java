package org.hillel.persistence.jpa.repository;

import org.hillel.persistence.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface TripJPARepository extends CommonRepository<TripEntity,Long>, JpaSpecificationExecutor<TripEntity> {

    @Query("select t from TripEntity t inner join RouteEntity r on t.route = r.id where r.id=:routeId and t.departureDate=:date")
    List<TripEntity> findByRouteAndDate(@Param("routeId") Long routeId, @Param("date") LocalDate departure);

    @Query("select t from TripEntity t inner join RouteEntity r on t.route = r.id where r.id=:routeId and t.departureDate=:date and t.active = true ")
    List<TripEntity> findByRouteAndDateActive(@Param("routeId") Long routeId, @Param("date") LocalDate departure);
}