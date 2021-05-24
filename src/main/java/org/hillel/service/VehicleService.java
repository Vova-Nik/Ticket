package org.hillel.service;

import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.VehicleEntity;
import org.hillel.persistence.entity.VehicleEntity_;
import org.hillel.persistence.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service("VehicleService")
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Transactional
    public Long save(final VehicleEntity entity) {
        if (entity == null || !entity.isValid())
            throw new IllegalArgumentException("VehicleService.save - VehicleEntity is not valid");
        return vehicleRepository.createOrUpdate(entity).getId();
    }

    @Transactional(readOnly = true)
    public List<VehicleEntity> getByName(final String name) {
        if (name == null) throw new IllegalArgumentException("VehicleService.getByName");
        return vehicleRepository.findByName(name);
    }

    @Transactional
    public void deleteById(final Long id) throws UnableToRemove {
        if(Objects.nonNull(id)) {
            vehicleRepository.removeById(id);
        }
    }

    @Transactional
    public void delete(final VehicleEntity vehicleEntity) throws UnableToRemove {
        if(Objects.nonNull(vehicleEntity)) {
            vehicleRepository.remove(vehicleEntity);
        }
    }

    @Transactional(readOnly = true)
    public boolean exists(final Long id){
        return vehicleRepository.exists(id);
    }

    @Transactional
    public List<VehicleEntity> findAllByNamedQuery(){
        return vehicleRepository.findAllByNamedQuery();
    }

    @Transactional(readOnly = true)
    public List<VehicleEntity> findAll() {
        return (List<VehicleEntity>)vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<VehicleEntity> findAllSQL() {
        return (List<VehicleEntity>)vehicleRepository.findAllSQL();
    }

    @Transactional(readOnly = true)
    public List<VehicleEntity> findAllCriteria() {
        return (List<VehicleEntity>)vehicleRepository.findAllCriteria();
    }

    @Transactional(readOnly = true)
    public List<VehicleEntity> storedProcExecute() {
        return (List<VehicleEntity>)vehicleRepository.storedProcExecute();
    }

    @Transactional(readOnly = true)
    public List<VehicleEntity> getSortedByPage(int pageSize, int first, String sortBy){
        if (!checkSortingCriteria(sortBy))
            throw new IllegalArgumentException("VehicleService.getSorted insufficient sortBy parameter");
        return vehicleRepository.getSortedByPage(pageSize, first, sortBy, true).orElseGet(ArrayList::new);
    }

    @Transactional(readOnly = true)
    public List<VehicleEntity> getSorted(String sortBy){
        if (!checkSortingCriteria(sortBy))
            throw new IllegalArgumentException("VehicleService.getSorted insufficient sortBy parameter");
        return vehicleRepository.getSorted(sortBy, true).orElseGet(ArrayList::new);
    }
    boolean checkSortingCriteria(String sortBy) {
        return (sortBy.equals(VehicleEntity_.ID) || sortBy.equals(VehicleEntity_.NAME) || sortBy.equals(VehicleEntity_.ACTIVE) || sortBy.equals(VehicleEntity_.CREATION_DATE));
    }
}
