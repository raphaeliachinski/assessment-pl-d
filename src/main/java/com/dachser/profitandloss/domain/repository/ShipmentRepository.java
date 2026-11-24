package com.dachser.profitandloss.domain.repository;

import com.dachser.profitandloss.domain.model.Shipment;
import com.dachser.profitandloss.domain.model.ShipmentNumber;

import java.util.List;
import java.util.Optional;


public interface ShipmentRepository {

    Shipment save(final Shipment shipment);

    Optional<Shipment> findById(final Long id);

    Optional<Shipment> findByShipmentNumber(final ShipmentNumber shipmentNumber);

    List<Shipment> findAll();

    boolean existsByShipmentNumber(final ShipmentNumber shipmentNumber);

    void deleteById(final Long id);
}
