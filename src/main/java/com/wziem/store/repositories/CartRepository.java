package com.wziem.store.repositories;

import com.wziem.store.entities.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
}
