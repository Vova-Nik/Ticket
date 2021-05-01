package org.hillel.persistence.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
/*Route realization wich has Date of departure
Creating  when first ticket to this rout sold
 */

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "trips",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"route", "departure"})
)

public class TripEntity extends AbstractEntity<Long> {

    @Column(name = "name")
    private String name;
    @ManyToOne
    VehicleEntity vehicle;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route")
    private RouteEntity route;
    @Column(name = "tickets_overal")
    private int tickets;
    @Column(name = "tickets_sold")
    private int sold;
    @Column(name = "departure")
    private LocalDate departureDate;

    public TripEntity(final RouteEntity route, final VehicleEntity vehicle, final LocalDate date) {
        this.route = route;
        departureDate = date;
        this.name = route.getName();
        this.vehicle = vehicle;
        tickets = vehicle.getOveralCapacity();
        sold = 0;
    }

    public boolean sellTicket() {
        if (tickets > sold) {
            sold++;
            return true;
        }
        return false;
    }

    public int getAvailible() {
        return tickets - sold;
    }

    public Instant getDeparture(){
        Time departureTime = route.getDepartureTime();
        long secAfterMidnight = departureTime.getHours()*3600 + departureTime.getMinutes()*60;
        Instant departure = departureDate.atStartOfDay(ZoneId.of("GMT")).toInstant();
        return  departure.plusSeconds(secAfterMidnight);
    }

    @Override
    public boolean isValid() {
        return super.isValid() && route != null && route.isValid();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripEntity that = (TripEntity) o;
        if(this.getId() == null || that.getId()==null) return  false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "TripEntity{" +
                "name='" + name + '\'' +
                ", vehicle=" + vehicle +
                ", route=" + route +
                ", tickets=" + tickets +
                ", sold=" + sold +
                ", departureDate=" + departureDate +
                '}';
    }
}
