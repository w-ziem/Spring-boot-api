package com.wziem.store.controllers;


import com.wziem.store.dtos.*;
import com.wziem.store.entities.Cart;
import com.wziem.store.entities.CartItem;
import com.wziem.store.exceptions.CartNotFoundException;
import com.wziem.store.exceptions.ProductNotFoundException;
import com.wziem.store.mappers.CartItemResponseMapper;
import com.wziem.store.mappers.CartMapper;
import com.wziem.store.mappers.NewCartMapper;
import com.wziem.store.repositories.CartRepository;
import com.wziem.store.repositories.ProductRepository;
import com.wziem.store.services.CartService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartRepository cartRepository;
    private final CartItemResponseMapper cartItemResponseMapper;
    private final ProductRepository productRepository;
    private final NewCartMapper newCartMapper;
    private final CartMapper cartMapper;
    private final CartService cartService;


    @PostMapping
    public ResponseEntity<NewCartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartService.createCart();
        
        URI location = uriBuilder.path("/carts/{id}")
            .buildAndExpand(cartDto.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(cartDto);
    }



    @PostMapping("/{cartId}/items")
    @Transactional
    public ResponseEntity<?> addProductToCart(UriComponentsBuilder uriBuilder, @PathVariable(name = "cartId") UUID id,  @RequestBody Long productId) {

        var cartItemDto = cartService.addToCart(id, productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);

    }


    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID id) {
        var cartDto = cartService.getCart(id);
        return ResponseEntity.ok(cartDto);
    }


    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductRequest request, @PathVariable(name = "cartId") UUID cartId, @PathVariable(name = "productId") Long productId) {
        var cartItem = cartService.updateProduct(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartItemResponseMapper.toDto(cartItem));
    }


    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID cartId, @PathVariable Long productId){
        cartService.removeProduct(cartId, productId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> emptyCart(@PathVariable UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
        }

        cart.clearCart();
        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found in the cart"));
    }

}