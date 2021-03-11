package org.hillel.service;

import org.hillel.model.Journey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
public class InMemoryJorneyServiceImp implements JourneyService{
    private Map<String, List<Journey>> storage = new HashMap<>();
    {
        storage.put("odessa->kiev", createJourney("odessa", "kiev"));
        storage.put("kiev->odessa", createJourney("kiev", "odessa"));
        storage.put("lviv->kiev", createJourney("lviv", "kiev"));
    }

    private List<Journey> createJourney(String from, String to){
        return Arrays.asList(
                new Journey(from, to, LocalDate.now(), LocalDate.now().plusDays(1)),
                new Journey(from, to, LocalDate.now(), LocalDate.now().plusDays(2)),
                new Journey(from, to, LocalDate.now(), LocalDate.now().plusDays(3))
        );
    }

    public Collection<Journey> find(String stationFrom, String stationTo, LocalDate dateFrom, LocalDate dateTo){
        LocalDateTime departure = LocalDateTime.of(dateFrom,LocalTime.of(0,0));
        LocalDateTime arrival = LocalDateTime.of(dateTo,LocalTime.of(0,0));
        if(storage.isEmpty()) return Collections.emptyList();
        List<Journey> journays = storage.get(stationFrom + "->" + stationTo);
        if(journays == null || journays.isEmpty()) return Collections.emptyList();
        List<Journey> out = new ArrayList<>();
        for(Journey journay : journays){
            if(journay.getDeparture().equals(departure) && (journay.getArrival().isBefore(arrival) || journay.getArrival().equals(arrival))){
                out.add(journay);
            }
        }
        return Collections.unmodifiableList(out);
    }
}
