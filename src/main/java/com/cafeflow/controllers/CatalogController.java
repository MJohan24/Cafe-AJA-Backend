package com.cafeflow.controllers;

import com.cafeflow.dto.MenuItemResponse;
import com.cafeflow.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/menu", "/api/menu"})
public class CatalogController {

    private final PaymentService paymentService;

    public CatalogController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getMenuCatalog() {
        return ResponseEntity.ok(paymentService.getMenuCatalog());
    }
}
