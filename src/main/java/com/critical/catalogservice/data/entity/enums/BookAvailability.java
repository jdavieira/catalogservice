package com.critical.catalogservice.data.entity.enums;

import lombok.Getter;

@Getter
public enum BookAvailability {
    TO_BE_LAUNCHED(0),
    ON_PRE_ORDER(1),
    ON_ORDER(2),
    AVAILABLE(3);

    private final int value;

    BookAvailability(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}