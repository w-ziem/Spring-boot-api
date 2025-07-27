package com.wziem.store.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemResponseDto {
    private ProductSummaryDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}