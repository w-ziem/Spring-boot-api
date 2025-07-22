package com.wziem.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    @ToString.Exclude
    private Set<CartItem> cartItems = new HashSet<>();

    // Usuń pole totalPrice i adnotację @Column

    @PrePersist
    protected void onCreate() {
        this.dateCreated = LocalDate.now();
    }

    @Transient // Ta adnotacja informuje JPA, żeby ignorowało to pole przy mapowaniu do bazy
    public BigDecimal getTotalPrice() {
        return cartItems.stream()
            .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addCartItem(CartItem newItem) {
        this.cartItems.add(newItem);
    }
}