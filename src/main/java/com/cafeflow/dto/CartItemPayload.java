package com.cafeflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CartItemPayload {

        @JsonProperty("menu_id")
        private Long menuId;

        @NotBlank
        private String name;

        @NotNull
        @Min(1)
        private Integer quantity;

        @NotNull
        @DecimalMin("0.01")
        private BigDecimal price;

        public CartItemPayload() {
        }

        public Long getMenuId() {
                return menuId;
        }

        public void setMenuId(Long menuId) {
                this.menuId = menuId;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Integer getQuantity() {
                return quantity;
        }

        public void setQuantity(Integer quantity) {
                this.quantity = quantity;
        }

        public BigDecimal getPrice() {
                return price;
        }

        public void setPrice(BigDecimal price) {
                this.price = price;
        }

        public Long menuId() {
                return menuId;
        }

        public String name() {
                return name;
        }

        public Integer quantity() {
                return quantity;
        }

        public BigDecimal price() {
                return price;
        }
}
