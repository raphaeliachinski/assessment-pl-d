package com.dachser.profitandloss.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

// One entry (income and costs) of a shipment
public record EntryResponse(Long id,
                            String shipmentNumber,
                            BigDecimal income,
                            BigDecimal totalCosts,
                            BigDecimal profitOrLoss,
                            Instant createdAt) {
}
