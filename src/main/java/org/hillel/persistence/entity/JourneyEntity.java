package org.hillel.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Entity
@Data
//@NoArgsConstructor
@Table(name = "journey")
public class JourneyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "station_from", nullable = false)
    private String stationFrom;

    @Column(name = "station_to", nullable = false)
    private String stationTo;

    public JourneyEntity(String stationFrom, String stationTo) {
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
    }

    public JourneyEntity() {

    }

    public boolean isValid() {
      return StringUtils.hasText(stationFrom) && StringUtils.hasText(stationTo);
    }
}
