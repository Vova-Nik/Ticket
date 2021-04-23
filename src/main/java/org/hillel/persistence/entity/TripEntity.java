package org.hillel.persistence.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
/*Route realization wich has Date of departure
Creating  when first ticket to this rout sold
 */

@Entity
@Getter
@NoArgsConstructor
@Table(name = "trips")
public class TripEntity extends AbstractEntity<Long>{

    @ManyToOne
    VehicleEntity vehicle;
    @OneToOne
    private RouteEntity route;
    @Column(name="tickets_overal")
    private int tickets;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripEntity that = (TripEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
