package org.hillel.persistence.entity;


import javax.persistence.*;
/*Route realization wich has Date of departure
Creating  when first ticket to this rout sold
 */

@Entity
@Table(name = "trips")
public class TripEntity extends AbstractEntity<Long>{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    VehicleEntity vehicle;
    @OneToOne
    RouteEntity route;
    @Column(name="tickets_overal")
    int tickets;

    public  TripEntity(final RouteEntity route){
        setName(route.getName());
    }

    public  TripEntity(final JourneyEntity journey){
      RouteEntity route = journey.getRoute();
        setName(route.getName());
    }
    @Override
    public boolean isValid(){
        return super.isValid() && route!=null && route.isValid();
    }
}
/*
    BUSINES("BUSINES"),
    ECONOM("ECONOM"),
    NONE("COMON");
    */
