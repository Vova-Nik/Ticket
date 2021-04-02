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
@Table(name = "journey")
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

    @OneToOne
//    @Column(name="vehicle", nullable = false)
    VehicleEntity vehicle;

    @Column(name="stations")
    @OneToMany
    List<StationEntity> stations;// = new ArrayList<>();


    public void addStop(StationEntity station){
        stations.add(station);
    }

    public JourneyEntity(StationEntity stationFrom, StationEntity stationTo) {
        this.stationFrom = stationFrom.getName();
        this.stationTo = stationTo.getName();
        stations = new ArrayList<>();
        stations.add(stationFrom);
        stations.add(stationTo);
        //subject to chane
        this.departure = Instant.now().plusSeconds(3600);
        this.arrival = Instant.now().plusSeconds(36000);
        this.vehicle = new VehicleEntity();
    }

    //Just for testing constructor
    public JourneyEntity(String stationFrom, String stationTo) {
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;

        this.departure = Instant.now().plusSeconds(3600);
        this.arrival = Instant.now().plusSeconds(36000);
        stations = new ArrayList<>();
        stations.add(new StationEntity("AnyFrom"));
        stations.add(new StationEntity("AnyTo"));
    }

    public boolean isValid() {
      if(StringUtils.hasText(stationFrom) && StringUtils.hasText(stationTo) && departure!=null && arrival!=null)
          return false;
//      if(stops.size()<2)
//          return false;
      return true;

    }
}
