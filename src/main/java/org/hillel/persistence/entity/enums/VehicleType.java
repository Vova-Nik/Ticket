package org.hillel.persistence.entity.enums;

public enum VehicleType {
    TRAIN("TRAIN"),
    BUS("BUS"),
    PLANE("PLANE"),
    CAR("CAR"),
    SHIP("SHIP");

    private String type;
    VehicleType(String type){
        this.type = type;
    }
}
