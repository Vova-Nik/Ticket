package org.hillel.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "journeys")
public class JourneyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_from", nullable = false, columnDefinition = "varchar(64)")
    private String stationFrom;

    @Column(name = "station_to", nullable = false)
    private String stationTo;

    @Column(name="departure", nullable = false)
    Instant departure;

    @Column(name="arrival", nullable = false)
    Instant arrival;

    @ManyToOne
    VehicleEntity vehicle;

    @OneToMany
    List<StationEntity> stations;   // = new ArrayList<>();

    public void addStop(final StationEntity station){
        stations.add(station);
    }

    public JourneyEntity(StationEntity stationFrom, StationEntity stationTo, VehicleEntity vehicle) {
        this.stationFrom = stationFrom.getName();
        this.stationTo = stationTo.getName();
        stations = new ArrayList<>();
        stations.add(stationFrom);
        stations.add(stationTo);
        this.vehicle = vehicle;
        //subject to chane
        this.departure = Instant.now().plusSeconds(3600);
        this.arrival = Instant.now().plusSeconds(36000);

    }

    public boolean isValid() {
        return StringUtils.hasText(stationFrom) && StringUtils.hasText(stationTo) && departure != null && arrival != null;
    }
}
