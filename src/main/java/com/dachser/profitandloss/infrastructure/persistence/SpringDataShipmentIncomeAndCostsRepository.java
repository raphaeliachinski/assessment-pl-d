package com.dachser.profitandloss.infrastructure.persistence;

import com.dachser.profitandloss.domain.model.ShipmentIncomeAndCosts;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

interface SpringDataShipmentIncomeAndCostsRepository extends JpaRepository<ShipmentIncomeAndCosts, Long> {

    List<ShipmentIncomeAndCosts> findByShipmentNumberOrderByCreatedAtDescIdDesc(final ShipmentNumber shipmentNumber);

    @Query("""
            select sum(e.income) as totalIncome, sum(e.totalCosts) as totalCosts
            from ShipmentIncomeAndCosts e
            where e.shipmentNumber = :shipmentNumber
            """)
    Sums sumByShipmentNumber(@Param("shipmentNumber") final ShipmentNumber shipmentNumber);

    // SUM returns null when the shipment has no entries
    interface Sums {
        BigDecimal getTotalIncome();

        BigDecimal getTotalCosts();
    }
}
