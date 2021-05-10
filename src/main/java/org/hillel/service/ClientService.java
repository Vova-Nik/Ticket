package org.hillel.service;

import org.hillel.persistence.entity.ClientEntity;
import org.hillel.persistence.jpa.repository.ClientJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ClientService")
public class ClientService extends EntityServiceImplementation<ClientEntity, Long>{

    private final ClientJPARepository clientRepository;

    @Autowired
    public ClientService(ClientJPARepository clientRepository){
        super(ClientEntity.class, clientRepository);
        this.clientRepository = clientRepository;
    }

    @Override
    boolean isValid(ClientEntity client) {
        return client.isValid();
    }

}
