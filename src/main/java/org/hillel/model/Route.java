package org.hillel.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    private int number;
    private String stationFrom;
    private String stationTo;
    private String depPeriod = "daily";
    @Getter(AccessLevel.NONE)
    private LocalTime departure;
    private int duration;           //trip duration in minutes
    private String info;

    public LocalTime getDeparture() {
        return LocalTime.of(departure.getHour(), departure.getMinute(), 0);
    }

    public LocalDateTime getDatedDeparture(LocalDate date) {
        return LocalDateTime.of(date, departure);
    }

    public LocalDateTime getDatedArrival(LocalDate date) {
        return LocalDateTime.of(date, departure).plusMinutes(duration);
    }
}
