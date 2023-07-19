package org.ikeda.store.web.ejb;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ikeda.store.core.CartItems;
import org.ikeda.store.service.CartService;

import java.util.List;

@RequestScoped
@Named
public class CartBean {

    @Inject
    private CartService cartService;

    private String cartId;

    private List<CartItems> items;

    public CartService getCartService() {
        return cartService;
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<CartItems> getItems() {
        return items;
    }

    public void setItems(List<CartItems> items) {
        this.items = items;
    }
}
