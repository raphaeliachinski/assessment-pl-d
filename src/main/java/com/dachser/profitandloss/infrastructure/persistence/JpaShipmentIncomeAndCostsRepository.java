package com.dachser.profitandloss.infrastructure.persistence;

import com.dachser.profitandloss.domain.model.ShipmentIncomeAndCosts;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import com.dachser.profitandloss.domain.model.ShipmentTotals;
import com.dachser.profitandloss.domain.repository.ShipmentIncomeAndCostsRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaShipmentIncomeAndCostsRepository implements ShipmentIncomeAndCostsRepository {

    private final SpringDataShipmentIncomeAndCostsRepository springDataRepository;

    JpaShipmentIncomeAndCostsRepository(final SpringDataShipmentIncomeAndCostsRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public ShipmentIncomeAndCosts save(final ShipmentIncomeAndCosts entry) {
        return springDataRepository.save(entry);
    }

    @Override
    public Optional<ShipmentIncomeAndCosts> findById(final Long id) {
        return springDataRepository.findById(id);
    }

    @Override
    public List<ShipmentIncomeAndCosts> findByShipmentNumber(final ShipmentNumber shipmentNumber) {
        return springDataRepository.findByShipmentNumberOrderByCreatedAtDescIdDesc(shipmentNumber);
    }

    @Override
    public ShipmentTotals sumByShipmentNumber(final ShipmentNumber shipmentNumber) {
        final SpringDataShipmentIncomeAndCostsRepository.Sums sums = springDataRepository.sumByShipmentNumber(shipmentNumber);
        return new ShipmentTotals(orZero(sums.getTotalIncome()), orZero(sums.getTotalCosts()));
    }

    private static BigDecimal orZero(final BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    @Override
    public void deleteById(final Long id) {
        springDataRepository.deleteById(id);
    }
}
