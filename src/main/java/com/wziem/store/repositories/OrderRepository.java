package com.wziem.store.repositories;

import com.wziem.store.entities.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderItems", "orderItems.product"})
    List<Order> findOrdersByCustomer_Id(Long userId);


    @EntityGraph(attributePaths = {"orderItems", "orderItems.product"})
    Order getOrderById(Long id);
}
