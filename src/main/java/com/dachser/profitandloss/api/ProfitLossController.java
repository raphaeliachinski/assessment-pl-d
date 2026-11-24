package com.dachser.profitandloss.api;

import com.dachser.profitandloss.application.ShipmentIncomeAndCostsService;
import com.dachser.profitandloss.application.ShipmentService;
import com.dachser.profitandloss.application.dto.CreateEntryRequest;
import com.dachser.profitandloss.application.dto.EntryResponse;
import com.dachser.profitandloss.application.dto.ShipmentProfitLossResponse;
import com.dachser.profitandloss.application.dto.ShipmentResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/shipments")
public class ProfitLossController {

    private final ShipmentService shipmentService;
    private final ShipmentIncomeAndCostsService shipmentIncomeAndCostsService;

    public ProfitLossController(final ShipmentService shipmentService,
                                final ShipmentIncomeAndCostsService shipmentIncomeAndCostsService) {
        this.shipmentService = shipmentService;
        this.shipmentIncomeAndCostsService = shipmentIncomeAndCostsService;
    }

    @GetMapping
    public List<ShipmentResponse> findAllShipments() {
        return shipmentService.findAll();
    }

    @PostMapping("/{number}/profit-loss")
    @ResponseStatus(HttpStatus.CREATED)
    public EntryResponse create(@PathVariable final String number,
                                @Valid @RequestBody final CreateEntryRequest request) {
        return shipmentIncomeAndCostsService.create(number, request);
    }

    @GetMapping("/{number}/profit-loss")
    public ShipmentProfitLossResponse findByShipmentNumber(@PathVariable final String number) {
        return shipmentIncomeAndCostsService.findByShipmentNumber(number);
    }
}
