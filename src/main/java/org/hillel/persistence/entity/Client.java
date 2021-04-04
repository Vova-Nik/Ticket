package org.hillel.persistence.entity;

public class Client {
    String name;
    String surname;
    String email;
    String pwd;
    public Client(){
        int num = (int)(Math.random()*1000);
        name = "Name";
        surname = "Surname"+ num;
        email = surname +"@.mail";
        pwd = "qwerty";
    }
}
