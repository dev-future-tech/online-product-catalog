package org.ikeda.store.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ikeda.store.core.Product;
import org.ikeda.store.service.ProductService;

import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/home")
public class StoreController {
    public static final Logger logger =
            Logger.getLogger(StoreController.class.getCanonicalName());

    @Inject
    ProductService productService;

    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Product> getProducts() {
        logger.info("Loading all products...");

        List<Product> allProducts = null;
        try {
            allProducts = productService.findAllProducts();
            if (allProducts == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            logger.log(Level.INFO, "Found {0} products", allProducts.size());
        } catch(Exception e) {
            logger.log(Level.SEVERE,
                    "Error calling findAllProducts() {0}",
                    new Object[]{e.getMessage()});
        }
        return allProducts;
    }

}
