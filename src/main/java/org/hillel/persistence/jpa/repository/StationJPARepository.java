package org.hillel.persistence.jpa.repository;

import org.hillel.persistence.entity.StationEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StationJPARepository extends CommonRepository<StationEntity,Long>, JpaSpecificationExecutor<StationEntity>{

}
