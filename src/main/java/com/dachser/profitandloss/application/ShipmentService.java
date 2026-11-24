package com.dachser.profitandloss.application;

import com.dachser.profitandloss.application.dto.ShipmentResponse;
import com.dachser.profitandloss.application.mappers.ShipmentMapper;
import com.dachser.profitandloss.domain.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper mapper;

    public ShipmentService(final ShipmentRepository shipmentRepository, final ShipmentMapper mapper) {
        this.shipmentRepository = shipmentRepository;
        this.mapper = mapper;
    }


    @Transactional(readOnly = true)
    public List<ShipmentResponse> findAll() {
        return shipmentRepository.findAll().stream()
                .map(mapper::toResponse)
                .sorted(Comparator.comparing(ShipmentResponse::shipmentNumber))
                .toList();
    }
}
