package org.hillel.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hillel.persistence.entity.enums.VehicleType;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "routes")
@DynamicUpdate
public class RouteEntity extends AbstractEntity<Long>{

    @Column(name = "station_from", nullable = false)
    String stationFrom;
    @Column(name = "station_to", nullable = false)
    String stationTo;

    @Column(name = "departure_period")
    String departurePeriod;

    @Column(name = "departure_time", nullable = false)
    Time departureTime;
    @Column(name = "arrival_time", nullable = false)
    Time arrivalTime;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

//    @ManyToMany(cascade = {CascadeType.PERSIST})
    @ManyToMany
    @JoinTable(name = "route_station",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns=@JoinColumn(name="station_id"))
    private List<StationEntity> stations;

    public RouteEntity(String routeNumber, StationEntity from, StationEntity to, Time departure, Time arrival) {
        this.setName(routeNumber);
        this.stationFrom = from.getName();
        this.stationTo = to.getName();
        this.departureTime = departure;
        this.arrivalTime = arrival;
        this.departurePeriod = "daily";
        this.type = VehicleType.TRAIN;
        this.stations = new ArrayList<>();
        stations.add(from);
        stations.add(to);
    }

    public void addStation(final StationEntity station) {
        if (!station.isValid()) throw new IllegalArgumentException("RouteEntity.addStation station object is not valid");
            stations.add(station);
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) return false;
        if (stationFrom == null) return false;
        if (departureTime == null) return false;
        if (type == null) return false;
        return true;
    }
}
