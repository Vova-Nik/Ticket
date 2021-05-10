package org.hillel.service;

import org.hillel.persistence.entity.AbstractEntity;
import org.hillel.persistence.jpa.repository.CommonRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class EntityServiceImplementation<E extends AbstractEntity<ID>, ID extends Serializable>{

    private final Class<E> entityClass;
    private CommonRepository<E, ID> repository;

    public EntityServiceImplementation(Class<E> entityClass, CommonRepository<E, ID> repository) {
        this.entityClass = entityClass;
        this.repository = repository;
    }

    @Transactional
    public E save(final E entity) {
        if (Objects.isNull(entity) || !entity.isValid())
            throw new IllegalArgumentException("EntityServiceImplementation illegal argument");
        return repository.save(entity);
    }

    @Transactional
    public void deleteById(final Long id) {
        if (Objects.nonNull(id))
            repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public E findById(Long id){
        if(Objects.isNull(id) || id<0) throw new IllegalArgumentException("EntityServiceImplementation.findById insufficient id");
        return repository.findById(id).orElseThrow(()->new IllegalArgumentException("EntityServiceImplementation.findById Entity with id = " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public boolean exists(final Long id) {
        return repository.existsById(id);
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public void saveList(final List<E> entities) {
        if (Objects.isNull(entities) || entities.size() == 0)
            throw new IllegalArgumentException("EntityServiceImplementation.saveList illegal List");
        entities.stream()
                .filter(this::isValid)
                .forEach((entity) -> repository.save(entity));
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public long countByNameActive(final String name) {
        return repository.countByNameAndActiveIsTrue(name);
    }

    @Transactional(readOnly = true)
    public List<E> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<E> findByIds(final Long... ids){
        if(Objects.isNull(ids) || ids.length==0) throw new IllegalArgumentException("EntityServiceImplementation.findByIds illegal  parameters");
        return repository.findAllById(Arrays.asList(ids));
    }

    @Transactional(readOnly = true)
    public List<E> findAllCtive(){
        return repository.findAllActive();
    }

    @Transactional
    public void disableById(final Long id){
        if(Objects.isNull(id) || id<0) throw new IllegalArgumentException("EntityServiceImplementation.disableById illegal id");
        repository.disableById(id);
    }

    @Transactional
    public void enableById(final Long id){
        if(Objects.isNull(id) || id<0) throw new IllegalArgumentException("EntityServiceImplementation.disableById illegal id");
        repository.enableById(id);
    }

    @Transactional(readOnly = true)
    public List<E> findByName(final String name){
        if(Objects.isNull(name) || name.length()<3) throw new IllegalArgumentException("EntityServiceImplementation.findByName illegal name parameter");
        return repository.findByName(name);
    }

    @Transactional(readOnly = true)
    public  List<E> findByNameActive(final String name){
        if(Objects.isNull(name) || name.length()<3) throw new IllegalArgumentException("EntityServiceImplementation.findByNameActive illegal name parameter");
        return repository.findByNameActive(name);
    }

    @Transactional(readOnly = true)
    public List<E> findSortedByPage(int pageNum, int size, final String columnToSortBy) {
        if (pageNum < 0 || size < 1)
            throw new IllegalArgumentException("EntityServiceImplementation.getSortedByPage illegal page parameters");
        if (!checkIfColumnExists(columnToSortBy))
            throw new IllegalArgumentException("EntityServiceImplementation.getSortedByPage illegal column name");
        return repository.findSortedByPage(PageRequest.of(pageNum, size, Sort.by(columnToSortBy)));
    }

    @Transactional(readOnly = true)
    public List<E> findActiveSortedByPage(int pageNum, int size, final String columnToSortBy) {
        if (pageNum < 0 || size < 1)
            throw new IllegalArgumentException("EntityServiceImplementation.getActiveSortedByPage illegal page parameters");
        if (!checkIfColumnExists(columnToSortBy))
            throw new IllegalArgumentException("EntityServiceImplementation.getActiveSortedByPage illegal column name");
        return repository.findActiveSortedByPage(PageRequest.of(pageNum, size, Sort.by(columnToSortBy)));
    }

    @Transactional(readOnly = true)
    public List<E> findAllByExample(Example<E> example){
        return repository.findAll(example);
    }


    abstract boolean isValid(E entity);

     boolean checkIfColumnExists(final String columnName) {
        Field[] fields;
        try {
            Class<?> clas = Class.forName(entityClass.getName());
            fields = clas.getDeclaredFields();
        } catch (java.lang.ClassNotFoundException e) {
            return false;
        }
        for (Field field : fields
        ) {
            if (field.getName().equals(columnName))
                return true;
        }
        return false;
    }

}
