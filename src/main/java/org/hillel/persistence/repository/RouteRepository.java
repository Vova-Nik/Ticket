package org.hillel.persistence.repository;

import org.hillel.persistence.entity.AbstractEntity;
import org.hillel.persistence.entity.RouteEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class RouteRepository extends ComonRepository<RouteEntity, Long>{
    protected RouteRepository(){
        super(RouteEntity.class);
    }
}
