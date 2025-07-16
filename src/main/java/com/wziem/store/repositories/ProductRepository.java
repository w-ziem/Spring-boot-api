package com.wziem.store.repositories;

import com.wziem.store.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"category"})
    List<Product> findByCategoryId(Byte categoryId);

    @EntityGraph(attributePaths = {"category"})
    List<Product> findAll();
}