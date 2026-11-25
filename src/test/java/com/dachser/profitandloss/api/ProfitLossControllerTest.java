package com.dachser.profitandloss.api;

import com.dachser.profitandloss.application.ShipmentIncomeAndCostsService;
import com.dachser.profitandloss.application.ShipmentService;
import com.dachser.profitandloss.application.dto.CreateEntryRequest;
import com.dachser.profitandloss.application.dto.EntryResponse;
import com.dachser.profitandloss.application.dto.ShipmentProfitLossResponse;
import com.dachser.profitandloss.application.dto.ShipmentResponse;
import com.dachser.profitandloss.domain.exception.DomainValidationException;
import com.dachser.profitandloss.domain.exception.ShipmentNotFoundException;
import com.dachser.profitandloss.domain.model.ShipmentNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfitLossController.class)
class ProfitLossControllerTest {

    private static final String URL = "/api/shipments/{shipmentNumber}/profit-loss";
    private static final Instant NOW = Instant.parse("2026-11-24T10:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentIncomeAndCostsService shipmentIncomeAndCostsService;

    @MockBean
    private ShipmentService shipmentService;

    private static EntryResponse response(final long id, final String income, final String totalCosts, final String profitOrLoss) {
        return new EntryResponse(id,
                "0001",
                new BigDecimal(income),
                new BigDecimal(totalCosts),
                new BigDecimal(profitOrLoss), NOW
        );
    }

    @Test
    void shouldCalculateProfitOrLoss() throws Exception {
        when(shipmentIncomeAndCostsService.create("0001",
                new CreateEntryRequest(
                new BigDecimal("230.5"),
                        new BigDecimal("200"),
                        new BigDecimal("0")
                )
        )
        )             .thenReturn(response(3L, "230.50", "200.00", "30.50"));

        mockMvc.perform(post(URL, "0001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"income": 230.5, "cost": 200, "additionalCost": 0}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.shipmentNumber").value("0001"))
                .andExpect(jsonPath("$.income").value(230.5))
                .andExpect(jsonPath("$.totalCosts").value(200.0))
                .andExpect(jsonPath("$.profitOrLoss").value(30.5))
                .andExpect(jsonPath("$.createdAt").value("2026-11-24T10:00:00Z"));
    }

    @Test
    void shouldAcceptRequestWithoutAdditionalCost() throws Exception {
        var request = new CreateEntryRequest(
                new BigDecimal("100"),
                new BigDecimal("150"),
                null
        );
        when(shipmentIncomeAndCostsService.create("0001", request))
                .thenReturn(response(4L, "100.00", "150.00", "-50.00"));

        mockMvc.perform(post(URL, "0001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"income": 100, "cost": 150}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.profitOrLoss").value(-50.0));
    }

    @Test
    void shouldRejectInvalidRequest() throws Exception {
        mockMvc.perform(post(URL, "0001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"income": -1, "cost": -1, "additionalCost": 1.234}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid data"))
                .andExpect(jsonPath("$.errors.income").value("Income must not be negative"))
                .andExpect(jsonPath("$.errors.cost").value("Cost must not be negative"))
                .andExpect(jsonPath("$.errors.additionalCost")
                        .value("Additional cost must have at most 15 integer digits and 2 decimal places"));

        verify(shipmentIncomeAndCostsService, never()).create(anyString(), any());
    }

    @Test
    void shouldRejectValuesThatWouldOverflowTheDatabaseColumns() throws Exception {
        mockMvc.perform(post(URL, "0001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"income": 1, "cost": 1000000000000000, "additionalCost": 0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.cost").value("Cost must have at most 15 integer digits and 2 decimal places"));

        verify(shipmentIncomeAndCostsService, never()).create(anyString(), any());
    }

    @Test
    void shouldRejectMissingRequiredFields() throws Exception {
        mockMvc.perform(post(URL, "0001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.income").value("Income is required"))
                .andExpect(jsonPath("$.errors.cost").value("Cost is required"));
    }

    @Test
    void shouldRejectMalformedJson() throws Exception {
        mockMvc.perform(post(URL, "0001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"income\":"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenShipmentDoesNotExist() throws Exception {
        when(shipmentIncomeAndCostsService.create(eq("9999"), any()))
                .thenThrow(new ShipmentNotFoundException(ShipmentNumber.of("9999")));

        mockMvc.perform(post(URL, "9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"income": 100, "cost": 50}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Shipment not found"))
                .andExpect(jsonPath("$.detail").value("Shipment 9999 not found"));
    }

    @Test
    void shouldReturnBadRequestWhenDomainRuleIsViolated() throws Exception {
        when(shipmentIncomeAndCostsService.create(anyString(), any()))
                .thenThrow(new DomainValidationException("Shipment number must have at most 20 characters"));

        mockMvc.perform(post(URL, "13127836123612863816778136")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"income": 100, "cost": 50}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Shipment number must have at most 20 characters"));
    }

    @Test
    void shouldReturnShipmentTotalsAndEntries() throws Exception {
        when(shipmentIncomeAndCostsService.findByShipmentNumber("0001")).thenReturn(new ShipmentProfitLossResponse(
                "0001", new BigDecimal("1500.00"), new BigDecimal("1100.00"), new BigDecimal("400.00"),
                List.of(response(1L, "1000.00", "200.00", "800.00"), response(2L, "500.00", "900.00", "-400.00"))));

        mockMvc.perform(get(URL, "0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentNumber").value("0001"))
                .andExpect(jsonPath("$.totalIncome").value(1500.0))
                .andExpect(jsonPath("$.totalCosts").value(1100.0))
                .andExpect(jsonPath("$.profitOrLoss").value(400.0))
                .andExpect(jsonPath("$.entries", hasSize(2)))
                .andExpect(jsonPath("$.entries[0].id").value(1))
                .andExpect(jsonPath("$.entries[1].profitOrLoss").value(-400.0));
    }

    @Test
    void shouldReturnShipmentWithoutEntries() throws Exception {
        when(shipmentIncomeAndCostsService.findByShipmentNumber("0002")).thenReturn(new ShipmentProfitLossResponse(
                "0002", new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), List.of()));

        mockMvc.perform(get(URL, "0002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profitOrLoss").value(0.0))
                .andExpect(jsonPath("$.entries", hasSize(0)));
    }

    @Test
    void shouldReturnNotFoundWhenGettingProfitLossOfUnknownShipment() throws Exception {
        when(shipmentIncomeAndCostsService.findByShipmentNumber("9999")).thenThrow(new ShipmentNotFoundException(ShipmentNumber.of("9999")));
        mockMvc.perform(get(URL, "9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListShipments() throws Exception {
        when(shipmentService.findAll()).thenReturn(List.of(new ShipmentResponse("0001"), new ShipmentResponse("0002")));

        mockMvc.perform(get("/api/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].shipmentNumber").value("0001"))
                .andExpect(jsonPath("$[1].shipmentNumber").value("0002"));
    }

}
