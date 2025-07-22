package com.wziem.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSummaryDto {
    private Long id;
    private String name;
    private BigDecimal price;
}
