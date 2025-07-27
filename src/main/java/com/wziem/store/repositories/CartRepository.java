package com.wziem.store.repositories;

import com.wziem.store.entities.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
    @EntityGraph(attributePaths = {"cartItems.product"})
    @Query("SELECT c FROM Cart c WHERE c.id = :cartId")
    Optional<Cart> getCartWithItems(@Param("cartId") UUID cardId);
}
