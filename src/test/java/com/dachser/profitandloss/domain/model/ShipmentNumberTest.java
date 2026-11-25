package com.dachser.profitandloss.domain.model;

import com.dachser.profitandloss.domain.exception.DomainValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShipmentNumberTest {

    @Test
    public void shouldFailWhenValueIsEmpty() {
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> ShipmentNumber.of(null)
        );
        assertEquals("Shipment number must not be empty", exception.getMessage());
    }


    @Test
    public void shouldFailWhenValueGreaterThanMaxLenght() {
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> ShipmentNumber.of("100000000000000000000")
        );

        assertEquals("Shipment number must have at most %d characters".formatted(ShipmentNumber.MAX_LENGTH), exception.getMessage());
    }
}
