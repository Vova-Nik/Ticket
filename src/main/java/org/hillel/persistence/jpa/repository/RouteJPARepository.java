package org.hillel.persistence.jpa.repository;

import org.hillel.persistence.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RouteJPARepository extends CommonRepository<RouteEntity,Long>, JpaSpecificationExecutor<RouteEntity> {

}

