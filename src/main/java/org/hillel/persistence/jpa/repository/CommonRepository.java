package org.hillel.persistence.jpa.repository;

import org.hillel.persistence.entity.TripEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@NoRepositoryBean
public interface CommonRepository<E extends Persistable<ID>, ID extends Serializable> extends JpaRepository<E, Long> {

    @Query("select e from #{#entityName} e where e.active = true")
    List<E> findAllActive();

    @Query("select e from #{#entityName} e where e.active = true and e.name = :name")
    List<E> findByNameActive(@Param("name") String name);

    @Modifying
    @Query("update #{#entityName} e set e.active = false where e.id=:id")
    void disableById(@Param("id") Long id);

    @Modifying
    @Query("update #{#entityName} e set e.active = true where e.id=:id")
    void enableById(@Param("id") Long id);

    long countByNameAndActiveIsTrue(String name);

    long count();

    @Query("select e from #{#entityName} e where  e.name=:name")
    List<E> findByName(@Param("name")String name);

    @Query("select e from #{#entityName} e ")
    List<E>  findSortedByPage(Pageable page);

    @Query("select e from #{#entityName} e where e.active = true ")
    List<E> findActiveSortedByPage(Pageable page);

}
