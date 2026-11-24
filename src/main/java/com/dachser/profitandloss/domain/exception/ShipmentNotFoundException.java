package com.dachser.profitandloss.domain.exception;

import com.dachser.profitandloss.domain.model.ShipmentNumber;

public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(final ShipmentNumber number) {
        super("Shipment %s not found".formatted(number.value()));
    }
}
