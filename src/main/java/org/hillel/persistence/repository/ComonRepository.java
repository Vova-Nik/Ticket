package org.hillel.persistence.repository;

import org.hibernate.Session;
import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.*;

public abstract class ComonRepository<E extends AbstractEntity<ID>, ID extends Serializable> implements GenericRepository<E, ID> {

    private final Class<E> entityClass;
    @PersistenceContext
    protected EntityManager entityManager;

    protected ComonRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public E createOrUpdate(E entity) {
        if (entity == null || !entity.isValid())
            throw new IllegalArgumentException("Repository.createOrUpdate not valid entity");
        if (Objects.isNull(entity.getId())) {
            entityManager.persist(entity);
            return entity;
        }
        return entityManager.merge(entity);
    }

    @Override
    public Optional<E> findById(ID id) {
        if (Objects.isNull(id)) throw new IllegalArgumentException("Repository.createOrUpdate not valid entity");
        E entity = entityManager.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public void removeById(ID id) throws UnableToRemove {
        if (id == null)
            throw new IllegalArgumentException("Repository.delete unsufficient entity");
        entityManager.remove(entityManager.getReference(entityClass, id));
    }

    @Override
    public void remove(E entity) throws UnableToRemove {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("Repository.delete insufficient id");
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            removeById(entity.getId());
        }
    }

    @SafeVarargs
    @Override
    public final Collection<E> findByIds(ID... ids) {
        if (ids.length > 1000)
            throw new IllegalArgumentException("Repository " + entityClass + " findByIds  too many IDs");
        return entityManager.unwrap(Session.class).byMultipleIds(entityClass).multiLoad(ids);
    }

    @Override
    public boolean exists(ID id) {
        Optional<E> entity = findById(id);
        return entity.isPresent();
    }

    @Override
    public Optional<Collection<E>> findAll() {
        return Optional.ofNullable(entityManager.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList());
    }

    @Override
    //    @SuppressWarnings("unchecked")
    public Optional<Collection<E>> findAllSQL() {
        List<E> list = new ArrayList<>();
        String entityName = entityClass.getAnnotation(Table.class).name();
        if (entityName.length() == 0) throw new IllegalArgumentException("findAllSQL class not found");
        List<?> result = entityManager.createNativeQuery("select * from " + entityName, entityClass).getResultList();
        if (result.size() == 0)
            return Optional.of(list);
        if (!result.get(0).getClass().equals(entityClass))
            return Optional.of(list);
        for (Object o : result) {
            list.add(entityClass.cast(o));
        }

        return Optional.of(list);
    }

    @Override
    public Optional<Collection<E>> findAllCriteria() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<E> query = criteriaBuilder.createQuery(entityClass);
        final Root<E> from = query.from(entityClass);
        return Optional.ofNullable(entityManager.createQuery(query.select(from)).getResultList());
    }

    @Override
    public Optional<Collection<E>> storedProcExecute() {
        List<E> list = new ArrayList<>();
        List<?> result = entityManager.createStoredProcedureQuery("find_all", entityClass)
                .registerStoredProcedureParameter(1, Class.class, ParameterMode.REF_CURSOR)
                .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
                .setParameter(2, entityClass.getAnnotation(Table.class).name())
                .getResultList();
        if (result.size() == 0)
            return Optional.of(list);
        if (!result.get(0).getClass().equals(entityClass))
            return Optional.of(list);
        for (Object o : result) {
            list.add(entityClass.cast(o));
        }
        return Optional.of(list);
    }

}


/*
create or replace function find_all(p_db_name IN varchar(20)) returns refcursor as
$$
declare
    db_cursor refcursor;
begin
    open db_cursor for execute format('select * from %I', p_db_name);
    return db_cursor;
end ;
$$ language plpgsql;
 */

