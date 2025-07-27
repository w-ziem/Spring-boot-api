package com.wziem.store.controllers;


import com.wziem.store.dtos.*;
import com.wziem.store.entities.Cart;
import com.wziem.store.entities.CartItem;
import com.wziem.store.mappers.CartItemResponseMapper;
import com.wziem.store.mappers.CartMapper;
import com.wziem.store.mappers.NewCartMapper;
import com.wziem.store.repositories.CartRepository;
import com.wziem.store.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<NewCartDto> createCart(UriComponentsBuilder uriBuilder) {
        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        
        // Użyj mapera do przekształcenia encji na DTO
        NewCartDto cartDto = newCartMapper.toDto(cart);
        
        URI location = uriBuilder.path("/carts/{id}")
            .buildAndExpand(cart.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Transactional
    public ResponseEntity<?> addProductToCart(UriComponentsBuilder uriBuilder, @PathVariable(name = "cartId") UUID id,  @RequestBody Long productId) {
        //czy koszyk istnieje
        var cart = cartRepository.getCartWithItems(id).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        //czy istnieje produkt
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Product dosen't exists");
        }

        var cartItem = cart.addCartItem(product);

        cartRepository.save(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
        return ResponseEntity.created(uri).body(cartItemResponseMapper.toDto(cartItem));

    }


    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID id) {
        var cart = cartRepository.getCartWithItems(id).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        var cartDto = cartMapper.toDto(cart);
        return ResponseEntity.ok(cartDto);
    }


    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductRequest request, @PathVariable(name = "cartId") UUID cartId, @PathVariable(name = "productId") Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
        }

        var cartItem = cart.getCartItem(productId);

        if(cartItem == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart item not found"));
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return ResponseEntity.ok(cartItemResponseMapper.toDto(cartItem));
    }


    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID cartId, @PathVariable Long productId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
        }

        var cartItem = cart.getCartItem(productId);

        if(cartItem == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart item not found"));
        }

        cart.removeCartItem(productId);
        cartRepository.save(cart);

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





}