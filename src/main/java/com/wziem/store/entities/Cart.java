package com.wziem.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
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

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.MERGE}, orphanRemoval = true)
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
            .map(CartItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add); //equivalent of declaring total = BigDecimal.ZERO and iteratind (adding)
    }


    public CartItem getCartItem(Long productId) {
        return cartItems.stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(null);
    }

    public CartItem addCartItem(Product product){
        //czy produkt jest juz w koszyku
        var cartItem = this.getCartItem(product.getId());

        //tworzenie produktu jeżeli nie istnieje
        if(cartItem != null) {
            cartItem.increaseQuantity();
        } else{
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(this);
            cartItem.setQuantity(1);
            cartItems.add(cartItem);
        }

        return cartItem;

    }

    public void removeCartItem(Long productId) {
        var cartItem = this.getCartItem(productId);
        if(cartItem != null){
            this.getCartItems().remove(cartItem);
            cartItem.setCart(null); //orphan removaal will delete this item automatically because it dosent have a cart assaigned
        }
    }

    public void clearCart() {
        if(cartItems != null) {
//            cartItems.forEach(item -> item.setCart(null)); // nie trzeba, bo jpa/hibernate traktuje rzeczy usuniete Z KOLEKCJI PO STRONIE RODZICA jako oprhan, a nie dzieci z usunieta referencja do rodzica
            cartItems.clear();
        }
    }
}