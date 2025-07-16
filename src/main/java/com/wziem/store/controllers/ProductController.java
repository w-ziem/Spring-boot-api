package com.wziem.store.controllers;

import com.wziem.store.entities.Category;
import com.wziem.store.mappers.ProductMapper;
import com.wziem.store.repositories.CategoryRepository;
import com.wziem.store.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wziem.store.dtos.ProductDto;
import org.springframework.web.util.UriComponentsBuilder;

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
        //could also return empty list when given wrong argument
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

    @PostMapping
    @Transactional
    public ResponseEntity<ProductDto> createProduct(UriComponentsBuilder uriBuilder, @RequestBody ProductDto request){

        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if(category == null){ return ResponseEntity.badRequest().build(); }

        var product = productMapper.toEntity(request);
        product.setCategory(category);

        productRepository.save(product);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productMapper.toDto(product));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto request) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) return ResponseEntity.notFound().build();

        productMapper.update(request, product);

        if (request.getCategoryId() != null) {
            var category = categoryRepository.findById(request.getCategoryId()).orElse(new Category(Byte.MIN_VALUE));
            if (category.getId() == Byte.MIN_VALUE) {
                return ResponseEntity.badRequest().build();
            }
            product.setCategory(category);
        }

        productRepository.save(product);
        return ResponseEntity.ok(productMapper.toDto(product));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }



}

