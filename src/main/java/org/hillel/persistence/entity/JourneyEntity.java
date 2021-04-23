package org.hillel.persistence.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@DynamicUpdate
@Getter
@NoArgsConstructor
@Table(name = "journeys")
public class JourneyEntity extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StationEntity stationFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StationEntity stationTo;

    @Column(name = "departure", nullable = false)
    private Instant departure;

    @Column(name = "arrival", nullable = false)
    private Instant arrival;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle")
    private VehicleEntity vehicleEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private RouteEntity route;

    public JourneyEntity(final RouteEntity route, final StationEntity stationFrom, final StationEntity stationTo, final VehicleEntity vehicle) {

        this.setName(stationFrom.getName() + "->" + stationTo.getName());
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.vehicleEntity = vehicle;

        this.departure = Instant.now().plusSeconds(3600);
        this.arrival = Instant.now().plusSeconds(36000);
        this.route = route;
    }

    public JourneyEntity(final RouteEntity route, final StationEntity stationFrom, final StationEntity stationTo) {
        this.setName(stationFrom.getName() + "->" + stationTo.getName());
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.departure = Instant.now().plusSeconds(3600);
        this.arrival = Instant.now().plusSeconds(36000);
        this.route = route;
    }

    @Override
    public boolean isValid() {
        return stationFrom.isValid() && stationTo.isValid() && departure != null && arrival != null;
    }

    public void setVehicle(final VehicleEntity vehicle) {
        if (Objects.nonNull(vehicle) && vehicle.isValid()) {
            this.vehicleEntity = vehicle;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof JourneyEntity)) return false;
        JourneyEntity entity = (JourneyEntity) o;
        return getId() != null && Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(departure, arrival, stationFrom, stationTo, vehicleEntity, route);
    }

}
