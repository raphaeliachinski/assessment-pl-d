package com.dachser.profitandloss.application;

import com.dachser.profitandloss.application.dto.CreateEntryRequest;
import com.dachser.profitandloss.application.dto.EntryResponse;
import com.dachser.profitandloss.application.dto.ShipmentProfitLossResponse;
import com.dachser.profitandloss.application.mappers.ProfitLossMapper;
import com.dachser.profitandloss.domain.exception.DomainValidationException;
import com.dachser.profitandloss.domain.exception.ShipmentNotFoundException;
import com.dachser.profitandloss.domain.model.ShipmentIncomeAndCosts;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import com.dachser.profitandloss.domain.service.ProfitLossCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipmentIncomeAndCostsServiceTest {

    private static final ShipmentNumber SHIPMENT = ShipmentNumber.of("0001");
    private final InMemoryShipmentRepository shipmentRepository = new InMemoryShipmentRepository().with("0001");
    private final InMemoryShipmentIncomeAndCostsRepository incomeAndCostsRepository = new InMemoryShipmentIncomeAndCostsRepository();

    private final ShipmentIncomeAndCostsService service = new ShipmentIncomeAndCostsService(
            shipmentRepository,
            incomeAndCostsRepository,
            new ProfitLossCalculator(),
            new ProfitLossMapper()
    );

    private static CreateEntryRequest entryRequest(final String income, final String cost, final String additionalCost) {
        return new CreateEntryRequest(
                new BigDecimal(income),
                new BigDecimal(cost),
                additionalCost == null
                        ? null
                        : new BigDecimal(additionalCost)
        );
    }

    @Test
    void shouldCalculateAndStoreProfitOrLoss() {
        EntryResponse response = service.create("0001", entryRequest("230.5", "200", "10"));

        assertEquals(1, incomeAndCostsRepository.fakeDb.size());
        ShipmentIncomeAndCosts saved = incomeAndCostsRepository.fakeDb.get(0);
        assertEquals(SHIPMENT, saved.getShipmentNumber());
        assertEquals(new BigDecimal("230.50"), saved.getIncome());
        assertEquals(new BigDecimal("200.00"), saved.getCost());
        assertEquals(new BigDecimal("10.00"), saved.getAdditionalCost());
        assertEquals(new BigDecimal("210.00"), saved.getTotalCosts());
        assertEquals(new BigDecimal("20.50"), saved.getProfitOrLoss());

        assertEquals("0001", response.shipmentNumber());
        assertEquals(new BigDecimal("230.50"), response.income());
        assertEquals(new BigDecimal("210.00"), response.totalCosts());
        assertEquals(new BigDecimal("20.50"), response.profitOrLoss());
        assertEquals(saved.getCreatedAt(), response.createdAt());
    }

    @Test
    void shouldFailWhenShipmentDoesNotExist() {
        ShipmentNotFoundException exception = assertThrows(ShipmentNotFoundException.class,
                () -> service.create(
                        "9999",
                        entryRequest("10", "1", null)
                )
        );

        assertEquals("Shipment 9999 not found", exception.getMessage());
        assertTrue(incomeAndCostsRepository.fakeDb.isEmpty());
    }

    @Test
    void shouldFailWhenShipmentNumberIsInvalid() {
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> service.findByShipmentNumber(" "));

        assertEquals("Shipment number must not be empty", exception.getMessage());
    }

    @Test
    void shouldFailWhenRequiredFieldsAreMissing() {
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> service.create(
                        "0001",
                        entryRequest("0", "0", null)
                )
        );

        assertEquals("Income, cost or additional cost must be greater than zero", exception.getMessage());
        assertTrue(incomeAndCostsRepository.fakeDb.isEmpty());
    }



    @Test
    void shouldReturnShipmentTotalsAndEntriesMostRecentFirst() {
        service.create("0001", entryRequest("1000", "200", null));
        service.create("0001", entryRequest("500", "800", "100"));

        ShipmentProfitLossResponse response = service.findByShipmentNumber("0001");

        assertEquals("0001", response.shipmentNumber());
        assertEquals(new BigDecimal("1500.00"), response.totalIncome());
        assertEquals(new BigDecimal("1100.00"), response.totalCosts());
        assertEquals(new BigDecimal("400.00"), response.profitOrLoss());
        assertEquals(List.of(
                        new BigDecimal("-400.00"),
                        new BigDecimal("800.00")
                ),
                response.entries()
                        .stream()
                        .map(EntryResponse::profitOrLoss)
                        .toList()
        );
    }

    @Test
    void shouldReturnZeroTotalsForShipmentWithoutEntries() {
        ShipmentProfitLossResponse response = service.findByShipmentNumber("0001");

        assertEquals(new BigDecimal("0.00"), response.profitOrLoss());
        assertTrue(response.entries().isEmpty());
    }

}
