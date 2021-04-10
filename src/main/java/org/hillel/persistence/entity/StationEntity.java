package org.hillel.persistence.entity;

import lombok.*;
import org.hillel.persistence.entity.enums.StationType;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.Instant;

//@AbstractEntity
//@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "stations")
public class StationEntity extends AbstractEntity<Long> {

    @Column(name = "longitude", nullable = false)
    Double longitude;
    @Column(name = "latitude", nullable = false)
    Double latitude;
    @Column(name = "description")
    String description;
    @Column(name = "foundation")
    Instant foundation;
    @Column(name = "station_type", nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private StationType stationType;

    public StationEntity(String name) {
        super.setName(name);
        longitude = 29.0D;
        latitude = 50.5D;
        description = "Just other Station on ones way";
        int delay = (int) (Math.random() * 1000000000);
        foundation = Instant.parse("1970-12-03T10:15:30.00Z").minusSeconds(delay * delay);
        stationType = StationType.LINEAR;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && StringUtils.hasText(getName()) && longitude != null && latitude != null && StringUtils.hasText(description) && foundation != null;
    }
}
