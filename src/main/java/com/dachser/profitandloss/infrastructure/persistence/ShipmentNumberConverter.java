package com.dachser.profitandloss.infrastructure.persistence;

import com.dachser.profitandloss.domain.model.ShipmentNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ShipmentNumberConverter implements AttributeConverter<ShipmentNumber, String> {

    @Override
    public String convertToDatabaseColumn(final ShipmentNumber shipmentNumber) {
        return shipmentNumber == null ? null : shipmentNumber.value();
    }

    @Override
    public ShipmentNumber convertToEntityAttribute(final String value) {
        return value == null ? null : ShipmentNumber.of(value);
    }
}
