package com.wziem.store.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class NewCartDto {
    private UUID id;
    private LocalDate dateCreated;
    private Set<CartItemResponseDto> cartItems = new HashSet<>();
    private BigDecimal totalPrice;

    // Metoda do przeliczania totalPrice na podstawie cartItems
    public void calculateTotalPrice() {
        if (cartItems != null) {
            this.totalPrice = cartItems.stream()
                .map(CartItemResponseDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }
}