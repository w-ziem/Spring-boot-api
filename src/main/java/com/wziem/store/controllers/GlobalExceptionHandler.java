package com.wziem.store.controllers;


import com.wziem.store.exceptions.CartIsEmptyException;
import com.wziem.store.exceptions.CartNotFoundException;
import com.wziem.store.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    // "name" : "NameIsRequired"
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException  exception) {
        var errors = new HashMap<String, String>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
        {errors.put(error.getField(), error.getDefaultMessage());
        } );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found in the cart"));
    }

    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<Map<String, String>> handleCartIsEmtpy() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Cart is empty. Please add products to the cart before checking out."));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = ex.getMessage();
        if (message.contains("UUID")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid UUID format. Please provide a valid UUID."));
        }
        return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid request format. Please check your request body."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        System.err.println("Unexpected error occured: " + ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An unexpected error occurred. Please try again later."));
    }
}