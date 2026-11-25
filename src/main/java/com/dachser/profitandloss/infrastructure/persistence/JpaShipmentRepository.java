package com.dachser.profitandloss.infrastructure.persistence;

import com.dachser.profitandloss.domain.model.Shipment;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import com.dachser.profitandloss.domain.repository.ShipmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaShipmentRepository implements ShipmentRepository {

    private final SpringDataShipmentRepository springDataRepository;

    JpaShipmentRepository(final SpringDataShipmentRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Shipment save(final Shipment shipment) {
        return springDataRepository.save(shipment);
    }

    @Override
    public Optional<Shipment> findById(final Long id) {
        return springDataRepository.findById(id);
    }

    @Override
    public Optional<Shipment> findByShipmentNumber(final ShipmentNumber shipmentNumber) {
        return springDataRepository.findByShipmentNumber(shipmentNumber);
    }

    @Override
    public List<Shipment> findAll() {
        return springDataRepository.findAllByOrderByShipmentNumberAsc();
    }

    @Override
    public boolean existsByShipmentNumber(final ShipmentNumber shipmentNumber) {
        return springDataRepository.existsByShipmentNumber(shipmentNumber);
    }

    @Override
    public void deleteById(final Long id) {
        springDataRepository.deleteById(id);
    }
}
