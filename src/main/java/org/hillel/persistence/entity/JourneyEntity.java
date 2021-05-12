package org.hillel.persistence.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Entity
@DynamicUpdate
@Getter
@NoArgsConstructor
@Table(name = "journeys")
public class JourneyEntity extends AbstractEntity<Long> {

    @Column(name="name")
    private String name;

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
    private RouteEntity route;

    public JourneyEntity(final RouteEntity route, final StationEntity stationFrom, final StationEntity stationTo, final LocalDate date) {
        if(Objects.isNull(route) || !route.isValid()) throw new IllegalArgumentException("JourneyEntity.constructor route not valid");
        if(Objects.isNull(stationFrom) || !route.isValid()) throw new IllegalArgumentException("JourneyEntity.constructor stationFrom not valid");
        if(Objects.isNull(stationTo) || !route.isValid()) throw new IllegalArgumentException("JourneyEntity.constructor stationTo not valid");

        this.name = stationFrom.getName() + "->" + stationTo.getName();
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.route = route;
        Time departureTime = route.getDepartureTime();
        long secAfterMidnight = departureTime.getHours()*3600 + departureTime.getMinutes()*60;
        departure = date.atStartOfDay(ZoneId.of("GMT")).toInstant();
        departure = departure.plusSeconds(secAfterMidnight);
        this.arrival = departure.plusSeconds(route.getDuration());
    }

    @Override
    public boolean isValid() {
        return stationFrom.isValid() && stationTo.isValid() && departure != null && arrival != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JourneyEntity that = (JourneyEntity) o;
        return stationFrom.equals(that.stationFrom) &&
                stationTo.equals(that.stationTo) &&
                route.equals(that.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationFrom, stationTo, route);
    }

    @Override
    public String toString() {
        return "JourneyEntity{" +
                "name='" + name + '\'' +
                ", departure=" + departure +
                ", arrival=" + arrival +
                '}';
    }
}
