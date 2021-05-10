package org.hillel.persistence.jpa.repository;

import org.hillel.persistence.entity.StationEntity;
import org.hillel.persistence.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface StationJPARepository extends CommonRepository<StationEntity,Long>, JpaSpecificationExecutor<StationEntity>{

}
