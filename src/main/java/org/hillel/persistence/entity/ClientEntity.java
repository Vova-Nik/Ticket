package org.hillel.persistence.entity;

import lombok.Getter;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "client")
@Getter

public class ClientEntity extends AbstractEntity<Long> {

    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;
    @Column(name = "pwd")
    private String pwd;

    public ClientEntity(String name, String surname) {
        super.setName(name);
        this.surname = surname;
        email = surname + "@.mail";
        pwd = "qwerty";
    }

    public ClientEntity() {
        int num = (int) (Math.random() * 1000);
        setName("Name");
        surname = "Surname" + num;
        email = surname + "@.mail";
        pwd = "qwerty" + num;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && StringUtils.hasText(surname) && StringUtils.hasText(email) && StringUtils.hasText(pwd);
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof ClientEntity)) return false;
        ClientEntity entity = (ClientEntity) o;
        return getId() != null && Objects.equals(getId(), entity.getId());
    }
    @Override
    public String getName(){
        return super.getName();
    }
    @Override
    public void setName(String name){
       super.setName(name);
    }
}
