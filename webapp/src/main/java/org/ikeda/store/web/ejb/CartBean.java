package org.ikeda.store.web.ejb;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.ikeda.store.core.CartItems;
import org.ikeda.store.service.CartService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Named
public class CartBean {

    private final Logger log = Logger.getLogger(CartBean.class.getCanonicalName());


    @Inject
    private CartService cartService;

    private String cartId;

    private Long productId;

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void addToCart() {
        log.log(Level.INFO, "Adding productId {0} to cart {1}", new Object[]{productId, cartId});

        this.cartService.addToCart(productId, 1, cartId);
    }
}
