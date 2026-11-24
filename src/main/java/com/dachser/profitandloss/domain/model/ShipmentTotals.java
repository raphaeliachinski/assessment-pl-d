package com.dachser.profitandloss.domain.model;

import java.math.BigDecimal;

// Sums of all entries of a shipment, calculated on read (SUM) and never stored
public record ShipmentTotals(BigDecimal totalIncome, BigDecimal totalCosts) {
}
