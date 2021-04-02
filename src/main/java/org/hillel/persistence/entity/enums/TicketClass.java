package org.hillel.persistence.entity.enums;

public enum TicketClass {

    BUSINES("BUSINES"),
    ECONOM("ECONOM"),
    NONE("COMON");

    private String title;

    TicketClass(String title) {
        this.title = title;
    }
}
