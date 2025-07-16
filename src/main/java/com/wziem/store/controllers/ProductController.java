package com.wziem.store.controllers;

import com.wziem.store.mappers.ProductMapper;
import com.wziem.store.repositories.CategoryRepository;
import com.wziem.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wziem.store.dtos.ProductDto;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;


    @GetMapping
    public ResponseEntity<List<ProductDto>> findAllProducts(
            @RequestParam(name = "categoryId", required = false, defaultValue = "0") byte id) {

        if (id == 0) {
            var result = productRepository.findAll().stream()
                    .map(productMapper::toDto)
                    .collect(toList());
            return ResponseEntity.ok(result);
        }

        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        var result = productRepository.findByCategoryId(id).stream()
                .map(productMapper::toDto)
                .collect(toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }



}

