package org.hillel.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Persistable;
import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Persistable<ID> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @Column(name = "creation_date")
    @CreationTimestamp
    private Instant creationDate;

    @Column
    boolean active;

    public AbstractEntity() {
        active = true;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    public boolean isValid() {
        return true;
    }

}
