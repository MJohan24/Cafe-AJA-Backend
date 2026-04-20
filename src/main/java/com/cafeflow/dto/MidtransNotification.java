package com.cafeflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MidtransNotification {

        @JsonProperty("order_id")
        private String orderId;

        @JsonProperty("transaction_status")
        private String transactionStatus;

        @JsonProperty("payment_type")
        private String paymentType;

        @JsonProperty("transaction_time")
        private String transactionTime;

        @JsonProperty("fraud_status")
        private String fraudStatus;

        public MidtransNotification() {
        }

        public String getOrderId() {
                return orderId;
        }

        public void setOrderId(String orderId) {
                this.orderId = orderId;
        }

        public String getTransactionStatus() {
                return transactionStatus;
        }

        public void setTransactionStatus(String transactionStatus) {
                this.transactionStatus = transactionStatus;
        }

        public String getPaymentType() {
                return paymentType;
        }

        public void setPaymentType(String paymentType) {
                this.paymentType = paymentType;
        }

        public String getTransactionTime() {
                return transactionTime;
        }

        public void setTransactionTime(String transactionTime) {
                this.transactionTime = transactionTime;
        }

        public String getFraudStatus() {
                return fraudStatus;
        }

        public void setFraudStatus(String fraudStatus) {
                this.fraudStatus = fraudStatus;
        }

        public String orderId() {
                return orderId;
        }

        public String transactionStatus() {
                return transactionStatus;
        }

        public String paymentType() {
                return paymentType;
        }

        public String transactionTime() {
                return transactionTime;
        }

        public String fraudStatus() {
                return fraudStatus;
        }
}
