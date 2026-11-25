package com.dachser.profitandloss.domain.service;

import com.dachser.profitandloss.domain.exception.DomainValidationException;
import com.dachser.profitandloss.domain.model.ShipmentIncomeAndCosts;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProfitLossCalculatorTest {

    private static final ShipmentNumber SHIPMENT = ShipmentNumber.of("0001");

    private final ProfitLossCalculator calculator = new ProfitLossCalculator();

    private ShipmentIncomeAndCosts calculate(final String income, final String cost, final String additionalCost) {
        return calculator.calculate(SHIPMENT, amount(income), amount(cost), amount(additionalCost));
    }

    private static BigDecimal amount(final String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @Test
    void shouldSumCostAndAdditionalCostIntoTotalCosts() {
        assertEquals(new BigDecimal("9.00"), calculate("10", "7", "2").getTotalCosts());
    }

    @Test
    void shouldRejectMissingCost() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> calculate("100", null, "0"));
        assertEquals("Cost is required", exception.getMessage());
    }

    @Test
    void shouldRejectNegativeCost() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> calculate("100", "-0.01", "0"));
        assertEquals("Cost must not be negative", exception.getMessage());
    }

    @Test
    void shouldRejectNegativeAdditionalCost() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> calculate("100", "0", "-5"));
        assertEquals("Additional cost must not be negative", exception.getMessage());
    }

    @Test
    void shouldAcceptEntryWithOnlyCosts() {
        final ShipmentIncomeAndCosts result = calculate("0", "150", "0");
        assertEquals(new BigDecimal("0.00"), result.getIncome());
        assertEquals(new BigDecimal("-150.00"), result.getProfitOrLoss());
    }

    @Test
    void shouldAcceptEntryWithOnlyIncome() {
        assertEquals(new BigDecimal("300.00"), calculate("300", "0", null).getProfitOrLoss());
    }

    @Test
    void shouldRejectEntryWithNothingInformed() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> calculate("0", "0", "0"));
        assertEquals("Income, cost or additional cost must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldRejectMissingIncome() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> calculate(null, "200", "0"));
        assertEquals("Income is required", exception.getMessage());
    }

    @Test
    void shouldRejectNegativeIncome() {
        DomainValidationException exception = assertThrows(DomainValidationException.class, () -> calculate("-1", "200", "0"));
        assertEquals("Income must not be negative", exception.getMessage());
    }

}
