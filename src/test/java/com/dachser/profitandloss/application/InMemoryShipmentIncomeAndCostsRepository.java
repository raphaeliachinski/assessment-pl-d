package com.dachser.profitandloss.application;

import com.dachser.profitandloss.domain.model.ShipmentIncomeAndCosts;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import com.dachser.profitandloss.domain.model.ShipmentTotals;
import com.dachser.profitandloss.domain.repository.ShipmentIncomeAndCostsRepository;

import java.math.BigDecimal;
import java.util.*;

class InMemoryShipmentIncomeAndCostsRepository implements ShipmentIncomeAndCostsRepository {

    final List<ShipmentIncomeAndCosts> fakeDb = new ArrayList<>();

    @Override
    public ShipmentIncomeAndCosts save(final ShipmentIncomeAndCosts entry) {
        fakeDb.add(entry);
        return entry;
    }

    @Override
    public Optional<ShipmentIncomeAndCosts> findById(final Long id) {
        return fakeDb.stream().filter(entry -> Objects.equals(id, entry.getId())).findFirst();
    }


    @Override
    public List<ShipmentIncomeAndCosts> findByShipmentNumber(final ShipmentNumber shipmentNumber) {
        List<ShipmentIncomeAndCosts> entries = new ArrayList<>(entriesOf(shipmentNumber));
        Collections.reverse(entries);
        return entries;
    }

    @Override
    public ShipmentTotals sumByShipmentNumber(final ShipmentNumber shipmentNumber) {
        List<ShipmentIncomeAndCosts> entries = entriesOf(shipmentNumber);
        return new ShipmentTotals(
                entries.stream().map(ShipmentIncomeAndCosts::getIncome).reduce(BigDecimal.ZERO, BigDecimal::add),
                entries.stream().map(ShipmentIncomeAndCosts::getTotalCosts).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    @Override
    public void deleteById(final Long id) {
        fakeDb.removeIf(entry -> Objects.equals(id, entry.getId()));
    }

    private List<ShipmentIncomeAndCosts> entriesOf(final ShipmentNumber shipmentNumber) {
        return fakeDb.stream()
                .filter(entry -> entry.getShipmentNumber().equals(shipmentNumber))
                .toList();
    }
}
