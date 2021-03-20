package org.hillel.persistence.entity;

import javax.persistence.*;

@Entity
//@Data
//@Transactional
@Table(name = "journey")
public class JourneyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="station_from", nullable = false)
    private String stationFrom;

//    @Column(name="station_to", nullable = false)
//    private String stationTo;

//    public JourneyEntity(String stationFrom, String stationTo) {
//        this.stationFrom = stationFrom;
//        this.stationTo = stationTo;
//    }
//
//    public boolean isValid(){
//        return  id>-2 && stationFrom != null && stationTo !=null && stationFrom.length()>2 && stationTo.length() >2;
//    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStationFrom() {
        return stationFrom;
    }

    public void setStationFrom(String stationFrom) {
        this.stationFrom = stationFrom;
    }
}
