package com.dachser.profitandloss.infrastructure.persistence;

import com.dachser.profitandloss.domain.model.Shipment;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface SpringDataShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByShipmentNumber(final ShipmentNumber shipmentNumber);

    boolean existsByShipmentNumber(final ShipmentNumber shipmentNumber);

    List<Shipment> findAllByOrderByShipmentNumberAsc();
}
