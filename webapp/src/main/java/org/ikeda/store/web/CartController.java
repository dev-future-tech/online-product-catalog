package org.ikeda.store.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.ikeda.store.service.CartService;

import java.util.logging.Logger;

@Path(("/cart/v1"))
public class CartController {

    static final Logger logger = Logger.getLogger(CartController.class.getCanonicalName());

    @Inject
    CartService cartService;

    @PUT
    @Path("/item/{productId}")
    public void addProductToCard(@PathParam("productId") Long productId, @QueryParam("qty") Integer quantity,
                                 @QueryParam("cartId") String cartId) {

    }

}
