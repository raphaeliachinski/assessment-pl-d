package com.dachser.profitandloss.domain.model;

import com.dachser.profitandloss.domain.exception.DomainValidationException;


public record ShipmentNumber(String value) {

    // database column size
    public static final int MAX_LENGTH = 20;

    public ShipmentNumber {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("Shipment number must not be empty");
        }
        value = value.strip();
        if (value.length() > MAX_LENGTH) {
            throw new DomainValidationException("Shipment number must have at most %d characters".formatted(MAX_LENGTH));
        }
    }

    public static ShipmentNumber of(final String value) {
        return new ShipmentNumber(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
