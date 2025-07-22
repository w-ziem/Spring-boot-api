package com.wziem.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateProductRequest {
    @Min(1)
    @Max(100)
    private Integer quantity;
}
