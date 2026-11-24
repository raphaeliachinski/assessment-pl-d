package com.dachser.profitandloss.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shipment_number", nullable = false, unique = true, length = ShipmentNumber.MAX_LENGTH)
    private ShipmentNumber shipmentNumber;

    protected Shipment() {
    }

    public Shipment(final ShipmentNumber shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }

    public Long getId() {
        return id;
    }

    public ShipmentNumber getShipmentNumber() {
        return shipmentNumber;
    }
}
