package com.wziem.store.mappers;

import com.wziem.store.dtos.NewCartDto;
import com.wziem.store.entities.Cart;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CartItemResponseMapper.class})
public interface NewCartMapper {

    @Mapping(target = "cartItems", source = "cartItems")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    NewCartDto toDto(Cart cart);

    @AfterMapping
    default void calculateTotalPrice(@MappingTarget NewCartDto cartDto) {
        cartDto.calculateTotalPrice();
    }
}