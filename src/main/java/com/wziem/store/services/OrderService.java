package com.wziem.store.services;

import com.wziem.store.entities.*;
import com.wziem.store.exceptions.CartIsEmptyException;
import com.wziem.store.exceptions.CartNotFoundException;
import com.wziem.store.repositories.CartRepository;
import com.wziem.store.repositories.OrderRepository;
import com.wziem.store.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    @Transactional
    public Order createOrderFromCart(UUID cartId, Long userId) {
        //nowy order
        var order = new Order();
        var cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException());
        var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if( cart.isEmpty() ) { throw new CartIsEmptyException(); }

        //mapowanie cartItems do orderItems
        var orderItems = new HashSet<OrderItem>();
        for (CartItem item : cart.getCartItems()) {
            var tempOrderItem = new OrderItem();
            tempOrderItem.setProduct(item.getProduct());
            tempOrderItem.setQuantity(item.getQuantity());
            tempOrderItem.setTotalPrice(item.getTotalPrice());
            tempOrderItem.setUnitPrice(item.getProduct().getPrice());
            tempOrderItem.setOrder(order);

            orderItems.add(tempOrderItem);
        }


        order.setOrderItems(orderItems);
        order.setCreatedAt(LocalDateTime.now());
        order.setCustomer(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());

        orderRepository.save(order);

        return order;
    }
}
