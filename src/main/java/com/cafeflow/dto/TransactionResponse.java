package com.cafeflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

        @JsonProperty("order_id")
        private final String orderId;

        @JsonProperty("customer_name")
        private final String customerName;

        @JsonProperty("gross_amount")
        private final BigDecimal grossAmount;

        private final String status;

        @JsonProperty("payment_type")
        private final String paymentType;

        @JsonProperty("created_at")
        private final LocalDateTime createdAt;

        public TransactionResponse(
                        String orderId,
                        String customerName,
                        BigDecimal grossAmount,
                        String status,
                        String paymentType,
                        LocalDateTime createdAt
        ) {
                this.orderId = orderId;
                this.customerName = customerName;
                this.grossAmount = grossAmount;
                this.status = status;
                this.paymentType = paymentType;
                this.createdAt = createdAt;
        }

        public String getOrderId() {
                return orderId;
        }

        public String getCustomerName() {
                return customerName;
        }

        public BigDecimal getGrossAmount() {
                return grossAmount;
        }

        public String getStatus() {
                return status;
        }

        public String getPaymentType() {
                return paymentType;
        }

        public LocalDateTime getCreatedAt() {
                return createdAt;
        }
}
