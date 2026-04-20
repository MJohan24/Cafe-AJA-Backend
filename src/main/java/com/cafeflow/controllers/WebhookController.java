package com.cafeflow.controllers;

import com.cafeflow.dto.MidtransNotification;
import com.cafeflow.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class WebhookController {

    private final PaymentService paymentService;

    public WebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping({"/api/v1/payments/webhook", "/api/webhooks/midtrans"})
    public ResponseEntity<Map<String, String>> receiveNotification(@RequestBody MidtransNotification payload) {
        paymentService.applyMidtransNotification(payload);
        return ResponseEntity.ok(Collections.singletonMap("message", "Webhook accepted"));
    }
}
