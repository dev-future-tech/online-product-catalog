package org.ikeda.store.core;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="customer_carts")
@NamedQuery(name="findAllCartItems", query="select items from CartItems items where items.cartId = :cartId")
@NamedQuery(name = "findAllCustomerCartItems", query = "select items from CartItems items where items.customerId = :customerId")
public class CartItems {

    @Id
    @Column(name="cart_id")
    private String cartId;

    @Column(name="customer_id")
    private Long customerId;

    @OneToMany(targetEntity = Product.class)
    @JoinTable(name="cart_contents", joinColumns={@JoinColumn(name="cart_id")}, inverseJoinColumns = {@JoinColumn(name="product_id")})
    private List<Product> items;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }
}
