package com.dachser.profitandloss.application.mappers;

import com.dachser.profitandloss.application.dto.ShipmentResponse;
import com.dachser.profitandloss.domain.model.Shipment;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapper {

    public ShipmentResponse toResponse(final Shipment shipment) {
        return new ShipmentResponse(shipment.getShipmentNumber().value());
    }
}
