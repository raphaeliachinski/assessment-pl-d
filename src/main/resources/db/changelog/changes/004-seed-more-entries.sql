--liquibase formatted sql

--changeset raphael:1
INSERT INTO shipment_income_and_costs (shipment_number, income, cost, additional_cost, total_costs, profit_or_loss, created_at)
VALUES ('0001', 0.00, 150.00, 0.00, 150.00, -150.00, TIMESTAMP WITH TIME ZONE '2026-11-20 09:00:00+00');


INSERT INTO shipment_income_and_costs (shipment_number, income, cost, additional_cost, total_costs, profit_or_loss, created_at)
VALUES ('0002', 800.00, 600.00, 50.00, 650.00, 150.00, TIMESTAMP WITH TIME ZONE '2026-11-20 10:00:00+00');
--rollback DELETE FROM shipment_income_and_costs WHERE created_at IN (TIMESTAMP WITH TIME ZONE '2026-11-20 09:00:00+00', TIMESTAMP WITH TIME ZONE '2026-11-20 10:00:00+00');
