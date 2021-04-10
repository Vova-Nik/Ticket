package org.hillel.service;

import org.hillel.persistence.entity.ClientEntity;
import org.hillel.persistence.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ClientService")
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    @Transactional
    public ClientEntity save(ClientEntity client){
        return clientRepository.createOrUpdate(client);
    }

//    @Transactional
//    public List<ClientEntity> getByName(String name){
//        return clientRepository.findByName("John");
//    }
}
