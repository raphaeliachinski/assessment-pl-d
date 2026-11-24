--liquibase formatted sql

--changeset raphael:1
INSERT INTO shipment (shipment_number) VALUES ('0001');
INSERT INTO shipment (shipment_number) VALUES ('0002');
INSERT INTO shipment (shipment_number) VALUES ('0003');
INSERT INTO shipment (shipment_number) VALUES ('0004');
INSERT INTO shipment (shipment_number) VALUES ('0005');
--rollback DELETE FROM shipment WHERE shipment_number IN ('0001', '0002', '0003', '0004', '0005');


--changeset raphael:2
INSERT INTO shipment_income_and_costs (shipment_number, income, cost, additional_cost, total_costs, profit_or_loss, created_at)
VALUES ('0001', 1000.00, 200.00, 0.00, 200.00, 800.00, TIMESTAMP WITH TIME ZONE '2026-11-10 09:00:00+00');

INSERT INTO shipment_income_and_costs (shipment_number, income, cost, additional_cost, total_costs, profit_or_loss, created_at)
VALUES ('0001', 500.00, 800.00, 100.00, 900.00, -400.00, TIMESTAMP WITH TIME ZONE '2026-11-11 09:00:00+00');
--rollback DELETE FROM shipment_income_and_costs WHERE shipment_number = '0001' AND created_at IN (TIMESTAMP WITH TIME ZONE '2026-11-10 09:00:00+00', TIMESTAMP WITH TIME ZONE '2026-11-11 09:00:00+00');
