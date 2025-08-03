package com.wziem.store.dtos;

import java.util.List;
import lombok.Data;

@Data
public class OrdersListDto {
    private List<OrderSummaryDto> orders;
}
