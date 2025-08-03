package com.wziem.store.controllers;


import com.wziem.store.dtos.CartCheckoutDto;
import com.wziem.store.dtos.OrderCheckoutDto;
import com.wziem.store.dtos.OrderSummaryDto;
import com.wziem.store.dtos.OrdersListDto;
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
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/orders")
    public ResponseEntity<OrdersListDto> getOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        var orders = orderRepository.findOrdersByCustomer_Id(userId);
        System.out.println(orders.size() + " orders");
        var responseDto = orderMapper.toOrderListDto(orders);
        System.out.println("Mapping done");
        return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderSummaryDto> getOrder(@PathVariable(name = "orderId") Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        var order = orderRepository.getOrderById(orderId);

        if(order == null){
            return ResponseEntity.notFound().build();
        }

        if(!order.getCustomer().getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(orderMapper.toOrderSummaryDto(order));
    }


}



