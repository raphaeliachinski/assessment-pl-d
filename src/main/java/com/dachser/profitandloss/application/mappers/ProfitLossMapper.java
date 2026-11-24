package com.dachser.profitandloss.application.mappers;

import com.dachser.profitandloss.application.dto.EntryResponse;
import com.dachser.profitandloss.application.dto.ShipmentProfitLossResponse;
import com.dachser.profitandloss.domain.model.ShipmentIncomeAndCosts;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import com.dachser.profitandloss.domain.model.ShipmentTotals;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class ProfitLossMapper {

    public EntryResponse toResponse(final ShipmentIncomeAndCosts entry) {
        return new EntryResponse(
                entry.getId(),
                entry.getShipmentNumber().value(),
                entry.getIncome(),
                entry.getTotalCosts(),
                entry.getProfitOrLoss(),
                entry.getCreatedAt());
    }

    public ShipmentProfitLossResponse toShipmentResponse(final ShipmentNumber shipmentNumber,
                                                         final ShipmentTotals totals,
                                                         final BigDecimal profitOrLoss,
                                                         final List<ShipmentIncomeAndCosts> entries) {
        return new ShipmentProfitLossResponse(
                shipmentNumber.value(),
                totals.totalIncome().setScale(2, RoundingMode.HALF_EVEN),
                totals.totalCosts().setScale(2, RoundingMode.HALF_EVEN),
                profitOrLoss,
                entries.stream().map(this::toResponse).toList());
    }
}
