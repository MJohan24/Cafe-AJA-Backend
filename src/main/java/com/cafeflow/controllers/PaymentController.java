package com.cafeflow.controllers;

import com.cafeflow.dto.CheckoutRequest;
import com.cafeflow.dto.CheckoutResponse;
import com.cafeflow.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/payments", "/api/payments"})
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping({"/initiate", "/snap-token"})
    public ResponseEntity<CheckoutResponse> createSnapToken(@Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(paymentService.createCheckout(request));
    }
}
