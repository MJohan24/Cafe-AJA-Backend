package com.cafeflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckoutResponse {

        @JsonProperty("order_id")
        private final String orderId;

        @JsonProperty("snap_token")
        private final String snapToken;

        @JsonProperty("redirect_url")
        private final String redirectUrl;

        private final String status;

        public CheckoutResponse(String orderId, String snapToken, String redirectUrl, String status) {
                this.orderId = orderId;
                this.snapToken = snapToken;
                this.redirectUrl = redirectUrl;
                this.status = status;
        }

        public String getOrderId() {
                return orderId;
        }

        public String getSnapToken() {
                return snapToken;
        }

        public String getRedirectUrl() {
                return redirectUrl;
        }

        public String getStatus() {
                return status;
        }
}
