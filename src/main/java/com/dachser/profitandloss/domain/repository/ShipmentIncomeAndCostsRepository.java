package com.dachser.profitandloss.domain.repository;

import com.dachser.profitandloss.domain.model.ShipmentIncomeAndCosts;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import com.dachser.profitandloss.domain.model.ShipmentTotals;

import java.util.List;
import java.util.Optional;

// Basic CRUD; save covers both create and update
public interface ShipmentIncomeAndCostsRepository {

    ShipmentIncomeAndCosts save(final ShipmentIncomeAndCosts entry);

    Optional<ShipmentIncomeAndCosts> findById(final Long id);

    // Entries of the shipment, most recent first
    List<ShipmentIncomeAndCosts> findByShipmentNumber(final ShipmentNumber shipmentNumber);

    // SUM of the income and of the total costs of all entries of the shipment (zero when there are none)
    ShipmentTotals sumByShipmentNumber(final ShipmentNumber shipmentNumber);

    void deleteById(final Long id);
}
