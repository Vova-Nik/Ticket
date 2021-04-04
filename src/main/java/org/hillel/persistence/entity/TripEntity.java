package org.hillel.persistence.entity;


import javax.persistence.*;
/*Route realization wich has Date of departure
Creating  when first ticket to this rout sold
 */
@Entity
@Table(name = "trips")
public class TripEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    VehicleEntity vehicle;
    @OneToOne
    RouteEntity route;
    @Column(name="tickets_overal")
    int tickets;


}
/*
    BUSINES("BUSINES"),
    ECONOM("ECONOM"),
    NONE("COMON");
    */
