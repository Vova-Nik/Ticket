package org.hillel.persistence.repository;

import org.hillel.persistence.entity.ClientEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository extends ComonRepository<ClientEntity, Long> {

    protected ClientRepository() {
        super(ClientEntity.class);
    }

}
