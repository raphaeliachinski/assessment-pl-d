package com.dachser.profitandloss.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "shipment_income_and_costs")
public class ShipmentIncomeAndCosts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shipment_number", nullable = false, length = ShipmentNumber.MAX_LENGTH)
    private ShipmentNumber shipmentNumber;

    @Column(name = "income", nullable = false, precision = 19, scale = 2)
    private BigDecimal income;

    @Column(name = "cost", nullable = false, precision = 19, scale = 2)
    private BigDecimal cost;

    @Column(name = "additional_cost", nullable = false, precision = 19, scale = 2)
    private BigDecimal additionalCost;

    @Column(name = "total_costs", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalCosts;

    @Column(name = "profit_or_loss", nullable = false, precision = 19, scale = 2)
    private BigDecimal profitOrLoss;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ShipmentIncomeAndCosts() {
    }


    public ShipmentIncomeAndCosts(final ShipmentNumber shipmentNumber,
                                  final BigDecimal income,
                                  final BigDecimal cost,
                                  final BigDecimal additionalCost,
                                  final BigDecimal totalCosts,
                                  final BigDecimal profitOrLoss,
                                  final Instant createdAt) {
        this.shipmentNumber = shipmentNumber;
        this.income = income;
        this.cost = cost;
        this.additionalCost = additionalCost;
        this.totalCosts = totalCosts;
        this.profitOrLoss = profitOrLoss;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public ShipmentNumber getShipmentNumber() {
        return shipmentNumber;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public BigDecimal getAdditionalCost() {
        return additionalCost;
    }

    public BigDecimal getTotalCosts() {
        return totalCosts;
    }

    public BigDecimal getProfitOrLoss() {
        return profitOrLoss;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
