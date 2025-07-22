package com.wziem.store.mappers;

import com.wziem.store.dtos.CartDto;
import com.wziem.store.entities.Cart;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CartItemResponseMapper.class})
public interface CartMapper {

    @Mapping(target = "id", expression = "java(cart.getId().toString())")
    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);
}