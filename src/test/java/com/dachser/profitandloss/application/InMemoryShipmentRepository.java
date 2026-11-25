package com.dachser.profitandloss.application;

import com.dachser.profitandloss.domain.model.Shipment;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import com.dachser.profitandloss.domain.repository.ShipmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class InMemoryShipmentRepository implements ShipmentRepository {

    private final List<Shipment> fakeDb = new ArrayList<>();

    public InMemoryShipmentRepository with(final String... shipmentNumbers) {
        for (String number : shipmentNumbers) {
            fakeDb.add(new Shipment(ShipmentNumber.of(number)));
        }
        return this;
    }

    @Override
    public Shipment save(final Shipment shipment) {
        fakeDb.add(shipment);
        return shipment;
    }

    @Override
    public Optional<Shipment> findById(final Long id) {
        return fakeDb.stream().filter(shipment -> Objects.equals(id, shipment.getId())).findFirst();
    }

    @Override
    public Optional<Shipment> findByShipmentNumber(final ShipmentNumber shipmentNumber) {
        return fakeDb.stream().filter(shipment -> shipment.getShipmentNumber().equals(shipmentNumber)).findFirst();
    }

    @Override
    public List<Shipment> findAll() {
        return List.copyOf(fakeDb);
    }

    @Override
    public boolean existsByShipmentNumber(final ShipmentNumber shipmentNumber) {
        return findByShipmentNumber(shipmentNumber).isPresent();
    }

    @Override
    public void deleteById(final Long id) {
        fakeDb.removeIf(shipment -> Objects.equals(id, shipment.getId()));
    }
}
