package com.dachser.profitandloss.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record ShipmentProfitLossResponse(String shipmentNumber,
                                         BigDecimal totalIncome,
                                         BigDecimal totalCosts,
                                         BigDecimal profitOrLoss,
                                         List<EntryResponse> entries) {
}
