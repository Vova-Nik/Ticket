package org.hillel.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor

public class ClientEntity extends AbstractEntity<Long> {

    @Column(name="name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;
    @Column(name = "pwd")
    private String pwd;

    public ClientEntity(String name, String surname) {
        this.name = name;
        this.surname = surname;
        email = surname + "@.mail";
        pwd = "qwerty";
    }

    public ClientEntity(String name) {
        int num = (int) (Math.random() * 1000);
        setName(name);
        surname = name + "son";
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
        return(email.equals(entity.email) && pwd.equals(entity.pwd));
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, pwd);
    }
}
