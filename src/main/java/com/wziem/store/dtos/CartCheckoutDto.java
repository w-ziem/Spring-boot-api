package com.wziem.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
public class CartCheckoutDto {
    @NotNull(message = "Cart ID is required")
    private UUID cartId;
}
