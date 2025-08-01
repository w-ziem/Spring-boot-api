package com.wziem.store.controllers;


import com.wziem.store.dtos.CartCheckoutDto;
import com.wziem.store.dtos.OrderCheckoutDto;
import com.wziem.store.entities.CartItem;
import com.wziem.store.entities.Order;
import com.wziem.store.entities.OrderItem;
import com.wziem.store.entities.OrderStatus;
import com.wziem.store.mappers.OrderMapper;
import com.wziem.store.repositories.CartRepository;
import com.wziem.store.repositories.OrderRepository;
import com.wziem.store.repositories.UserRepository;
import com.wziem.store.services.JwtService;
import com.wziem.store.services.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;

@AllArgsConstructor
@RestController
public class OrderController {

    private final JwtService jwtService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final OrderService orderService;


    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody CartCheckoutDto request) {
        //walidacja czy user zalogowany (token aktualny)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        //walidacja czy cart nie jest pusty
        var cart = cartRepository.findById(request.getCartId()).orElse(null);
        if(cart == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Cart not found"));
        }

        if( cart.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Cart is empty"));
        }

        var order = orderService.createOrderFromCart(request.getCartId(), userId);

        return ResponseEntity.ok(orderMapper.toDto(order.getId()));

    }




}



