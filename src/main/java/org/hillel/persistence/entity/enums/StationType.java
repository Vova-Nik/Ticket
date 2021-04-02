package org.hillel.persistence.entity.enums;

public enum StationType {
    LINEAR("One route station"),
    TRANSIT("Station with possibility to get to another route");

    private String title;

    StationType(String title) {
        this.title = title;
    }
}
