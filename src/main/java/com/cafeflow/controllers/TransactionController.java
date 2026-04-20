package com.cafeflow.controllers;

import com.cafeflow.dto.TransactionResponse;
import com.cafeflow.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/transactions", "/api/transactions"})
public class TransactionController {

    private final PaymentService paymentService;

    public TransactionController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/today")
    public ResponseEntity<List<TransactionResponse>> getTodayTransactions() {
        return ResponseEntity.ok(paymentService.getTodayTransactions());
    }
}
