package com.cafeflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class MenuItemResponse {

        private final Long id;
        private final String name;
        private final String category;
        private final BigDecimal price;
        private final Integer stock;

        @JsonProperty("image_url")
        private final String imageUrl;

        public MenuItemResponse(Long id, String name, String category, BigDecimal price, Integer stock, String imageUrl) {
                this.id = id;
                this.name = name;
                this.category = category;
                this.price = price;
                this.stock = stock;
                this.imageUrl = imageUrl;
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

        public BigDecimal getPrice() {
                return price;
        }

        public Integer getStock() {
                return stock;
        }

        public String getImageUrl() {
                return imageUrl;
        }
}
