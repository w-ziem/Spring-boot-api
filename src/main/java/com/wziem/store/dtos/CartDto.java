package com.wziem.store.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDto {
    private String id;
    private List<CartItemResponseDto> items;
    BigDecimal totalPrice;
}
