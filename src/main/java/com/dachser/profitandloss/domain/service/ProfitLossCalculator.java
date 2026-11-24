package com.dachser.profitandloss.domain.service;

import com.dachser.profitandloss.domain.exception.DomainValidationException;
import com.dachser.profitandloss.domain.model.ShipmentIncomeAndCosts;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import com.dachser.profitandloss.domain.model.ShipmentTotals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

/**
 * Calculation validations
 *
 */
public class ProfitLossCalculator {

    public ShipmentIncomeAndCosts calculate(final ShipmentNumber shipmentNumber,
                                            final BigDecimal income,
                                            final BigDecimal cost,
                                            final BigDecimal additionalCost) {
        if (income == null) {
            throw new DomainValidationException("Income is required");
        }
        if (income.signum() < 0) {
            throw new DomainValidationException("Income must not be negative");
        }
        if (cost == null) {
            throw new DomainValidationException("Cost is required");
        }
        if (cost.signum() < 0) {
            throw new DomainValidationException("Cost must not be negative");
        }

        final BigDecimal additional = additionalCost == null ? BigDecimal.ZERO : additionalCost;
        if (additional.signum() < 0) {
            throw new DomainValidationException("Additional cost must not be negative");
        }

        //At least one income or costs
        if (income.signum() == 0 && cost.signum() == 0 && additional.signum() == 0) {
            throw new DomainValidationException("Income, cost or additional cost must be greater than zero");
        }

        final BigDecimal totalCosts = cost.add(additional);
        final BigDecimal profitOrLoss = income.subtract(totalCosts);

        return new ShipmentIncomeAndCosts(
                shipmentNumber,
                scale(income),
                scale(cost),
                scale(additional),
                scale(totalCosts),
                scale(profitOrLoss),
                Instant.now());
    }

    public BigDecimal profitOrLoss(final ShipmentTotals totals) {
        return scale(totals.totalIncome().subtract(totals.totalCosts()));
    }

    private static BigDecimal scale(final BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_EVEN);
    }
}
