package org.hillel.persistence.jpa.repository;

import org.hillel.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ClientJPARepository extends CommonRepository<ClientEntity,Long>, JpaSpecificationExecutor<ClientEntity> {
}
