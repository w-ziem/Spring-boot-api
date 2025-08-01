package com.wziem.store.mappers;

import com.wziem.store.dtos.OrderCheckoutDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderCheckoutDto toDto(Long orderId);
}
