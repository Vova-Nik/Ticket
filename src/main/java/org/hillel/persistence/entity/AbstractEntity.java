package org.hillel.persistence.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass

public abstract class AbstractEntity<ID extends Serializable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @Column(name="name")
    private String name;

    @Column(name="creation_date")
    @CreationTimestamp
    private Instant creationDate;

    public AbstractEntity(){
    }

    public boolean isValid() {
        return StringUtils.hasText(name);
   }

}
