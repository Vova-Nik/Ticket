package org.hillel.persistence.repository;

import org.hibernate.Session;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.AbstractEntity;
import org.hillel.persistence.entity.VehicleEntity;
import org.springframework.util.StringUtils;
import javax.persistence.*;
import javax.persistence.criteria.*;
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
    public Collection<E> findAll() {
        return entityManager.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();
    }

    @Override
    public Collection<E>  findAllSQL() {
        List<E> list = new ArrayList<>();
        String entityName = entityClass.getAnnotation(Table.class).name();
        if (entityName.length() == 0) throw new IllegalArgumentException("findAllSQL class not found");
        List<?> result = entityManager.createNativeQuery("select * from " + entityName, entityClass).getResultList();
        if (result.size() == 0)
            return list;
        if (!result.get(0).getClass().equals(entityClass))
            return list;
        for (Object o : result) {
            list.add(entityClass.cast(o));
        }
        return list;
    }

    @Override
    public Collection<E> findAllCriteria() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<E> query = criteriaBuilder.createQuery(entityClass);
        final Root<E> from = query.from(entityClass);
        return entityManager.createQuery(query.select(from)).getResultList();
    }

    // SELECT prosrc FROM pg_proc WHERE proname = 'find_all';
    @Override
    public Collection<E> storedProcExecute() {
        List<E> list = new ArrayList<>();
        List<?> result = entityManager.createStoredProcedureQuery("find_all", entityClass)
                .registerStoredProcedureParameter(1, Class.class, ParameterMode.REF_CURSOR)
                .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
                .setParameter(2, entityClass.getAnnotation(Table.class).name())
                .getResultList();
        if (result.size() == 0)
            return list;
        if (!result.get(0).getClass().equals(entityClass))
            return list;
        for (Object o : result) {
            list.add(entityClass.cast(o));
        }
        return list;
    }

    @Override
    public List<E> findByNameActive(String name) {
        if(StringUtils.isEmpty(name)) throw new IllegalArgumentException("findByName bad name");
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<E> query = criteriaBuilder.createQuery(entityClass);
        final Root<E> from = query.from(entityClass);
        final Join<Object, Object> journeys = from.join("journeys", JoinType.LEFT);

        final Predicate byName = criteriaBuilder.equal(from.get("name"), criteriaBuilder.parameter(String.class, "nameParam"));
        final Predicate active = criteriaBuilder.equal(from.get("active"), criteriaBuilder.parameter(Boolean.class,"activeParam"));
        return entityManager.createQuery(query.select(from)
                .where(byName, active))
                .setParameter("nameParam", name)
                .setParameter("activeParam", true)
                .getResultList();
    }

    @Override
    public List<E> findByName(String name) {
        if(StringUtils.isEmpty(name)) throw new IllegalArgumentException("findByName bad name");
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<E> query = criteriaBuilder.createQuery(entityClass);
        final Root<E> from = query.from(entityClass);
        final Predicate byName = criteriaBuilder.equal(from.get("name"), criteriaBuilder.parameter(String.class, "nameParam"));
        return entityManager.createQuery(query.select(from).where(byName))
                .setParameter("nameParam", name)
                .getResultList();
    }

    public Optional<List<E>> getSorted(String sortBy, boolean ascending) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<E> query = criteriaBuilder.createQuery(entityClass);
        final Root<E> from = query.from(entityClass);
        final OrderImpl order = new OrderImpl(from.get(sortBy), ascending);
        List<E> stations = entityManager.createQuery(query.select(from)
                .orderBy(order)
        ).getResultList();
        if (stations.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(stations);
    }

    //https://www.youtube.com/watch?v=p_sFYoJ4A_E&ab_channel=ThorbenJanssen 3:50
    public Optional<List<E>> getSortedByPage(int pageSise, int first, String sortBy, boolean ascending) {
        if (pageSise < 2 || first < 0 || pageSise > 1000)
            throw new IllegalArgumentException("StationRepository.getSorted insufficient pageination parameters");
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        final Root<E> from = criteriaQuery.from(entityClass);
        final OrderImpl order = new OrderImpl(from.get(sortBy), ascending);
        criteriaQuery.orderBy(order);

        TypedQuery<E> typeQuery = entityManager.createQuery(criteriaQuery);
        List<E> stations = typeQuery
                .setFirstResult(first)
                .setMaxResults(pageSise)
                .getResultList();
        if (stations.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(stations);
    }

    @Override
    public List<E> findAllByNamedQuery(){
        return entityManager.createNamedQuery("findAll", entityClass).getResultList();
    }

    @Override
    public List<E> findAllByStoredProc(){
        List<?> entities = entityManager.createNamedStoredProcedureQuery("findAllVehicles").getResultList();
        if(entities.size()==0) throw  new IllegalArgumentException("findAllByStoredProc error");
        if(entities.get(0).getClass() != entityClass) throw  new IllegalArgumentException("findAllByStoredProc error");
        return (List<E>)entities;
    }
}



