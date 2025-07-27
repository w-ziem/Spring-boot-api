package com.wziem.store.services;


import com.wziem.store.dtos.CartDto;
import com.wziem.store.dtos.CartItemResponseDto;
import com.wziem.store.dtos.NewCartDto;
import com.wziem.store.entities.Cart;
import com.wziem.store.entities.CartItem;
import com.wziem.store.exceptions.CartNotFoundException;
import com.wziem.store.exceptions.ProductNotFoundException;
import com.wziem.store.mappers.CartItemResponseMapper;
import com.wziem.store.mappers.CartMapper;
import com.wziem.store.mappers.NewCartMapper;
import com.wziem.store.repositories.CartRepository;
import com.wziem.store.repositories.ProductRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final NewCartMapper newCartMapper;
    private final ProductRepository productRepository;
    private final CartItemResponseMapper cartItemResponseMapper;
    private final CartMapper cartMapper;

    public NewCartDto createCart(){
        var cart = new Cart();
        cart = cartRepository.save(cart);
        return newCartMapper.toDto(cart);
    }

    public CartItemResponseDto addToCart(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        //czy istnieje produkt
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addCartItem(product);

        cartRepository.save(cart);


        return cartItemResponseMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID id) {
        var cart = cartRepository.getCartWithItems(id).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        return cartMapper.toDto(cart);
    }

    public CartItem updateProduct(UUID cartId, Long productId, @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be greater than 0") @Max(value = 100, message = "Quantity must be less than 100") Integer quantity) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if(cart == null){
            throw new CartNotFoundException();
        }

        var cartItem = cart.getCartItem(productId);

        if(cartItem == null){
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartItem;
    }

    public void removeProduct(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if(cart == null){
            throw new CartNotFoundException();
        }

        var cartItem = cart.getCartItem(productId);

        if(cartItem == null){
            throw new ProductNotFoundException();
        }

        cart.removeCartItem(productId);
        cartRepository.save(cart);
    }
}
