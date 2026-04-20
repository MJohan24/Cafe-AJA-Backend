package com.cafeflow.services;

import com.cafeflow.dto.CartItemPayload;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MidtransService {

    private final ObjectMapper objectMapper;
    private final String serverKey;
    private final String baseUrl;
    private final RestTemplate restTemplate;

    public MidtransService(
            ObjectMapper objectMapper,
            @Value("${MIDTRANS_SERVER_KEY:}") String serverKey,
            @Value("${MIDTRANS_BASE_URL:https://app.sandbox.midtrans.com}") String baseUrl
    ) {
        this.objectMapper = objectMapper;
        this.serverKey = serverKey;
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    public SnapTokenResult requestSnapToken(
            String orderId,
            String customerName,
            BigDecimal grossAmount,
            List<CartItemPayload> items
    ) {
        if (serverKey == null || serverKey.trim().isEmpty()) {
            throw new IllegalStateException("MIDTRANS_SERVER_KEY belum diatur di environment backend.");
        }

        Map<String, Object> payload = new LinkedHashMap<>();

        Map<String, Object> transactionDetails = new LinkedHashMap<>();
        transactionDetails.put("order_id", orderId);
        transactionDetails.put("gross_amount", grossAmount.setScale(0, RoundingMode.HALF_UP).intValue());
        payload.put("transaction_details", transactionDetails);

        Map<String, Object> customerDetails = new LinkedHashMap<>();
        customerDetails.put("first_name", customerName);
        payload.put("customer_details", customerDetails);

        payload.put("item_details", toItemDetails(items));

        try {
            String requestBody = objectMapper.writeValueAsString(payload);
            String authorization = "Basic " + Base64.getEncoder()
                    .encodeToString((serverKey + ":").getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", authorization);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + "/snap/v1/transactions",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("Gagal membuat Snap Token Midtrans: " + response.getBody());
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            String token = root.path("token").asText("");
            String redirectUrl = root.path("redirect_url").asText("");
            if (token.trim().isEmpty() || redirectUrl.trim().isEmpty()) {
                throw new IllegalStateException("Respons Midtrans tidak berisi token/redirect_url.");
            }

            return new SnapTokenResult(token, redirectUrl);
        } catch (IOException ex) {
            throw new IllegalStateException("Terjadi kendala saat memanggil Midtrans.", ex);
        }
    }

    private List<Map<String, Object>> toItemDetails(List<CartItemPayload> items) {
        List<Map<String, Object>> details = new ArrayList<>();
        for (CartItemPayload item : items) {
            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("id", item.menuId() != null ? item.menuId().toString() : item.name());
            detail.put("price", item.price().setScale(0, RoundingMode.HALF_UP).intValue());
            detail.put("quantity", item.quantity());
            detail.put("name", item.name());
            details.add(detail);
        }
        return details;
    }

    public static class SnapTokenResult {
        private final String token;
        private final String redirectUrl;

        public SnapTokenResult(String token, String redirectUrl) {
            this.token = token;
            this.redirectUrl = redirectUrl;
        }

        public String getToken() {
            return token;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public String token() {
            return token;
        }

        public String redirectUrl() {
            return redirectUrl;
        }
    }
}
