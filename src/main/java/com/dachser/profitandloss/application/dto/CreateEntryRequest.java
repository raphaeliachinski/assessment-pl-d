package com.dachser.profitandloss.application.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;


public record CreateEntryRequest(

        @NotNull(message = "Income is required")
        @PositiveOrZero(message = "Income must not be negative")
        @Digits(integer = 15, fraction = 2,
                message = "Income must have at most 15 integer digits and 2 decimal places")
        BigDecimal income,

        @NotNull(message = "Cost is required")
        @PositiveOrZero(message = "Cost must not be negative")
        @Digits(integer = 15, fraction = 2, message = "Cost must have at most 15 integer digits and 2 decimal places")
        BigDecimal cost,

        @PositiveOrZero(message = "Additional cost must not be negative")
        @Digits(integer = 15, fraction = 2, message = "Additional cost must have at most 15 integer digits and 2 decimal places")
        BigDecimal additionalCost) {
}
