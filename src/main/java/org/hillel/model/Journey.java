package org.hillel.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@EqualsAndHashCode
public class Journey {
    private final String stationFrom;
    private final String stationTo;
    private final LocalDateTime departure;
    private final LocalDateTime arrival;
    private final String route;

    public Journey(final String stationFrom, final String stationTo, final LocalDateTime departure, final LocalDateTime arrival) {
        if (!StringUtils.hasText(stationFrom)) throw new IllegalArgumentException("Station from must be set");
        this.stationFrom = StringUtils.trimWhitespace(StringUtils.capitalize(stationFrom));
        if (!StringUtils.hasText(stationTo)) throw new IllegalArgumentException("Station to  must be set");
        this.stationTo = StringUtils.trimWhitespace(StringUtils.capitalize(stationTo));
        this.departure = departure;
        this.arrival = arrival;
        this.route = stationFrom + "->" + stationTo;
    }

    //compatibility to InMemoryJourneyServiceImpl
    public Journey(final String stationFrom, final String stationTo, final LocalDate departure, final LocalDate arrival) {
        if (!StringUtils.hasText(stationFrom)) throw new IllegalArgumentException("Station from must be set");
        this.stationFrom = StringUtils.trimWhitespace(StringUtils.capitalize(stationFrom));
        if (!StringUtils.hasText(stationTo)) throw new IllegalArgumentException("Station to  must be set");
        this.stationTo = StringUtils.trimWhitespace(StringUtils.capitalize(stationTo));
        this.departure = LocalDateTime.of(departure, LocalTime.of(0, 0, 0));
        this.arrival = LocalDateTime.of(arrival, LocalTime.of(0, 0, 0));
        this.route = stationFrom + "->" + stationTo;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("\n")
                .append("Journey\n")
                .append("Route - ")
                .append(route)
                .append("\n")
                .append("Departure ")
                .append(departure)
                .append("\n")
                .append("Arrival - ")
                .append(arrival)
                .append("\n").toString();
    }
}
