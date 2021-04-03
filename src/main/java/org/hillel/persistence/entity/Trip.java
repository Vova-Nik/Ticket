package org.hillel.persistence.entity;


import javax.persistence.*;
@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
