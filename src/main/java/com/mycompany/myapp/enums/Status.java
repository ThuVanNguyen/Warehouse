package com.mycompany.myapp.enums;

import java.util.stream.Stream;

public enum Status {
    BANG_TUONG_WARE_HOUSE("BANG_TUONG_WARE_HOUSE"),
    LOADED_ON_VEHICLE("LOADED_ON_VEHICLE"),
    ARRIVED_AT_HN("ARRIVED_AT_HN"),
    DELIVERED("DELIVERED"),
    CUSTOMER_SIGNED("CUSTOMER_SIGNED"),
    CANCELED("CANCELED");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public static Status parse(final String status) {
        return Stream.of(Status.values()).filter(e -> e.value.equals(status)).findFirst().orElse(Status.CANCELED);
    }

    public String getValue() {
        return value;
    }
}
