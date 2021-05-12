package org.hillel.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hillel.exceptions.UnableToRemove;
import org.hillel.persistence.entity.enums.VehicleType;
import javax.persistence.*;
import java.sql.Time;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "routes")

@DynamicUpdate
public class RouteEntity extends AbstractEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "station_from", nullable = false)
    private String stationFrom;

    @Column(name = "station_to", nullable = false)
    private String stationTo;

    @Column(name = "departure_period")
    private String departurePeriod;

    @Column(name = "departure_time", nullable = false)
    private Time departureTime;

    @Column(name = "duration", nullable = false)
    private long duration;

    @Column(name = "arrival_time", nullable = false)
    private Time arrivalTime;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = StationEntity.class)
    private Set<StationEntity> stations;

    public RouteEntity(final String routeNumber, final StationEntity from, final StationEntity to, final Time departure, long duration) {
        this.setName(routeNumber);
        this.stationFrom = from.getName();
        this.stationTo = to.getName();
        this.departureTime = departure;
        this.duration = duration;
        this.arrivalTime = new Time(departure.getTime() + duration);
        this.departurePeriod = "daily";
        this.type = VehicleType.TRAIN;
        this.stations = new HashSet<>();
        addStation(from);
        addStation(to);
    }

    public void addStation(final StationEntity station) {
        if (Objects.isNull(station))
            throw new IllegalArgumentException("RouteEntity.addStation station object is null");
        if (!station.isValid())
            throw new IllegalArgumentException("RouteEntity.addStation station object is not valid");
        if (stations.contains(station)) return;
        stations.add(station);
    }

    public void removeStation(final StationEntity station) throws UnableToRemove {
        if (Objects.isNull(station))
            throw new UnableToRemove("RouteEntity.removeStation station object is not valid");
        if (station.getName().equals(stationFrom))
            throw new UnableToRemove("RouteEntity.removeStation Attempt to delete \"from\" station");
        if (station.getName().equals(stationTo))
            throw new UnableToRemove("RouteEntity.removeStation Attempt to delete \"to\" station");
        stations.remove(station);
    }

    public boolean containsStation(final StationEntity stationToFind) {
        if (Objects.isNull(stationToFind))
            throw new IllegalArgumentException("RouteEntity.containsStation station object is null");
        if (!stationToFind.isValid())
            throw new IllegalArgumentException("RouteEntity.containsStation station object is not valid");
        for (StationEntity station : stations) {
            if (station.equals(stationToFind)) return true;
        }
        return false;
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) return false;
        if (stationFrom == null) return false;
        if (departureTime == null) return false;
        if (type == null) return false;
        return true;
    }

    public List<StationEntity> getStations() {
        return new ArrayList<>(stations);
    }

    public StationEntity getFromStation() {
        for (StationEntity station : stations) {
            if (station.getName().equals(this.stationFrom))
                return station;
        }
        throw new IllegalArgumentException("Route entity bad station From");
    }

    public StationEntity getToStation() {
        for (StationEntity station : stations) {
            if (station.getName().equals(this.stationFrom))
                return station;
        }
        throw new IllegalArgumentException("Route entity bad station To");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteEntity that = (RouteEntity) o;
        return name.equals(that.name) &&
                stationFrom.equals(that.stationFrom) &&
                stationTo.equals(that.stationTo) &&
                departureTime.equals(that.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, stationFrom, stationTo, departureTime);
    }
}
