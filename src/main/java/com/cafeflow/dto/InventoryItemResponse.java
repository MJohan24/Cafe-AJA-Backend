package com.cafeflow.dto;

import java.math.BigDecimal;

public class InventoryItemResponse {

        private final Long id;
        private final String name;
        private final String category;
        private final Integer stock;
        private final BigDecimal price;

        public InventoryItemResponse(Long id, String name, String category, Integer stock, BigDecimal price) {
                this.id = id;
                this.name = name;
                this.category = category;
                this.stock = stock;
                this.price = price;
        }

        public Long getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getCategory() {
                return category;
        }

        public Integer getStock() {
                return stock;
        }

        public BigDecimal getPrice() {
                return price;
        }
}
