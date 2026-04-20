package com.cafeflow.controllers;

import com.cafeflow.dto.InventoryItemResponse;
import com.cafeflow.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/inventory", "/api/inventory"})
public class InventoryController {

    private final PaymentService paymentService;

    public InventoryController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemResponse>> getInventory() {
        return ResponseEntity.ok(paymentService.getInventorySnapshot());
    }
}
