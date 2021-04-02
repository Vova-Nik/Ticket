package org.hillel.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hillel.persistence.entity.enums.StationType;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@ToString
@Table(name = "stations")
public class StationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "longitude", nullable = false)
    Double longitude;
    @Column(name = "latitude", nullable = false)
    Double latitude;
    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "foundation")
    Instant foundation;

    @Column(name = "station_type", nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private StationType stopType;

    public StationEntity(String name) {
        this.name = name;
        longitude = 29D;
        latitude = 51D;
        description = "Just a Station";
        int delay = (int) (Math.random() * 1000000000);
        foundation = Instant.parse("1970-12-03T10:15:30.00Z").minusSeconds(delay * delay);
        stopType = StationType.LINEAR;
    }

    //    static Instant	parse(CharSequence text)
    //    Obtains an instance of Instant from a text string such as 2007-12-03T10:15:30.00Z.
    public boolean isValid() {
        if (name == null) return false;
        //to change
        if (longitude == null) longitude = 30D;
        if (latitude == null) latitude = 50D;
        if (description == null || description.length() == 0) description = "Just a station on ones way";
        if (foundation == null) foundation = Instant.now().minusSeconds(3153600000L);
        if (stopType == null) stopType = StationType.LINEAR;
        return true;
    }
}
