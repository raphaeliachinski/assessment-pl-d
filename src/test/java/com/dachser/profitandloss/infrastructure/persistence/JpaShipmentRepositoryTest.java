package com.dachser.profitandloss.infrastructure.persistence;

import com.dachser.profitandloss.domain.model.Shipment;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(JpaShipmentRepository.class)
class JpaShipmentRepositoryTest {

    @Autowired
    private JpaShipmentRepository repository;

    @Test
    void shouldReturnAllShipmentsOrderedByNumber() {
        repository.save(new Shipment(ShipmentNumber.of("0000")));

        List<String> numbers = repository.findAll().stream()
                .map(shipment -> shipment.getShipmentNumber().value())
                .toList();

        assertEquals(List.of("0000", "0001", "0002", "0003", "0004", "0005"), numbers);
    }
}
