package org.hillel.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hillel.persistence.entity.enums.StationType;
import org.hillel.persistence.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "train_routes")
public class TrainRoutesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "route_number", nullable = false)
    Integer routeNumber;

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


    @Column(name = "stations")
    @OneToMany
    List<StationEntity> stations;


    public TrainRoutesEntity(Integer routeNumber,StationEntity from, StationEntity to, Time departure, Time arrival) {
        this.routeNumber = routeNumber;
        this.stationFrom = from.getName();
        this.stationTo = to.getName();
        this.departureTime = departure;
        this.arrivalTime = arrival;
        this.departurePeriod = "daily";
        this.stations = new ArrayList<>();
        stations.add(from);
        stations.add(to);
    }

    public boolean addStation(final StationEntity station) {
        if (station.isValid() && !stations.contains(station)) {
            stations.add(station);
            return true;
        }
        return false;
    }

    public boolean isValid() {
        if (routeNumber == null) return false;
        if (stationFrom == null) return false;
        if (departureTime == null) return false;
        return true;
    }
}
