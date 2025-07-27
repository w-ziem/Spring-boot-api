package com.wziem.store.mappers;

import com.wziem.store.dtos.CartItemResponseDto;
import com.wziem.store.dtos.ProductSummaryDto;
import com.wziem.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartItemResponseMapper {

    @Mapping(source = "product", target = "product")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemResponseDto toDto(CartItem cartItem);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    ProductSummaryDto toProductSummaryDto(com.wziem.store.entities.Product product);

}