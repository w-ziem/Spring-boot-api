package com.wziem.store.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private ProductSummaryDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
