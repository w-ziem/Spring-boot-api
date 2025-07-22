package com.wziem.store.repositories;

import com.wziem.store.entities.CartItem;
import org.springframework.data.repository.CrudRepository;

public interface CartItemRepository  extends CrudRepository<CartItem, Long> {
}
