package com.wziem.store.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemResponseDto {
    private ProductSummaryDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
    
    public void calculateTotalPrice() {
        if (product != null && product.getPrice() != null && quantity != null) {
            this.totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }
}