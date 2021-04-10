package org.hillel.persistence.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@AbstractEntity
@Entity
@DynamicUpdate
@Getter
@NoArgsConstructor
@Table(name = "journey")
public class JourneyEntity extends AbstractEntity<Long>{

    @Column(name = "departure", nullable = false)
    Instant departure;

    @Column(name = "arrival", nullable = false)
    Instant arrival;

    @ManyToOne(fetch = FetchType.LAZY)
    StationEntity stationFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    StationEntity stationTo;

    @ManyToOne(fetch = FetchType.LAZY)
    VehicleEntity vehicle;

    @ManyToOne
    RouteEntity route;

    public JourneyEntity(RouteEntity route, StationEntity stationFrom, StationEntity stationTo, VehicleEntity vehicle) {
        this.setName(stationTo.getName() + "->" + stationFrom.getName());
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.vehicle = vehicle;
        //subject to change
        this.departure = Instant.now().plusSeconds(3600);
        this.arrival = Instant.now().plusSeconds(36000);
        this.route = route;
    }

    @Override
    public boolean isValid() {
        return stationFrom.isValid() && stationTo.isValid() && departure != null && arrival != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JourneyEntity)) return false;
        JourneyEntity entity = (JourneyEntity) o;
        return getId() != null && Objects.equals(getId(), entity.getId());
    }
}
