package com.dachser.profitandloss.application;

import com.dachser.profitandloss.application.dto.CreateEntryRequest;
import com.dachser.profitandloss.application.dto.EntryResponse;
import com.dachser.profitandloss.application.dto.ShipmentProfitLossResponse;
import com.dachser.profitandloss.application.mappers.ProfitLossMapper;
import com.dachser.profitandloss.domain.exception.ShipmentNotFoundException;
import com.dachser.profitandloss.domain.model.ShipmentIncomeAndCosts;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import com.dachser.profitandloss.domain.model.ShipmentTotals;
import com.dachser.profitandloss.domain.repository.ShipmentIncomeAndCostsRepository;
import com.dachser.profitandloss.domain.repository.ShipmentRepository;
import com.dachser.profitandloss.domain.service.ProfitLossCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentIncomeAndCostsService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentIncomeAndCostsRepository incomeAndCostsRepository;
    private final ProfitLossCalculator calculator;
    private final ProfitLossMapper mapper;

    public ShipmentIncomeAndCostsService(final ShipmentRepository shipmentRepository,
                             final ShipmentIncomeAndCostsRepository incomeAndCostsRepository,
                             final ProfitLossCalculator calculator,
                             final ProfitLossMapper mapper) {
        this.shipmentRepository = shipmentRepository;
        this.incomeAndCostsRepository = incomeAndCostsRepository;
        this.calculator = calculator;
        this.mapper = mapper;
    }

    @Transactional
    public EntryResponse create(final String shipmentNumber, final CreateEntryRequest request) {
        ShipmentNumber number = getExistingShipment(shipmentNumber);
        ShipmentIncomeAndCosts incomeAndCosts = calculator.calculate(
                number, request.income(), request.cost(), request.additionalCost());
        ShipmentIncomeAndCosts saved = incomeAndCostsRepository.save(incomeAndCosts);

        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ShipmentProfitLossResponse findByShipmentNumber(final String shipmentNumber) {
        ShipmentNumber number = getExistingShipment(shipmentNumber);
        ShipmentTotals totals = incomeAndCostsRepository.sumByShipmentNumber(number);

        return mapper.toShipmentResponse(
                number,
                totals,
                calculator.profitOrLoss(totals),
                incomeAndCostsRepository.findByShipmentNumber(number));
    }

    private ShipmentNumber getExistingShipment(final String value) {
        ShipmentNumber shipmentNumber = ShipmentNumber.of(value);
        if (!shipmentRepository.existsByShipmentNumber(shipmentNumber)) {
            throw new ShipmentNotFoundException(shipmentNumber);
        }
        return shipmentNumber;
    }
}
