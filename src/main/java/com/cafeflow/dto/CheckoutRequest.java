package com.cafeflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CheckoutRequest {

        @JsonProperty("customer_name")
        @NotBlank
        private String customerName;

        @NotEmpty
        private List<@Valid CartItemPayload> items;

        public CheckoutRequest() {
        }

        public String getCustomerName() {
                return customerName;
        }

        public void setCustomerName(String customerName) {
                this.customerName = customerName;
        }

        public List<CartItemPayload> getItems() {
                return items;
        }

        public void setItems(List<CartItemPayload> items) {
                this.items = items;
        }

        public String customerName() {
                return customerName;
        }

        public List<CartItemPayload> items() {
                return items;
        }
}
