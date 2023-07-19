package org.ikeda.store.web.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Stateless;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import org.ikeda.store.core.Product;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named
@Stateless
public class ProductBean implements Serializable {

    protected Client client;
    private static final Logger logger
            = Logger.getLogger(ProductBean.class.getName());

    @PostConstruct
    private void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    private void clean() {
        client.close();
    }

    public List<Product> retrieveAllProducts() {
        logger.info("Loading all products...");
        return client.target("http://localhost:8080/online-product-catalog/webApi/home/all")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<>() {
                });
    }

    public Product findProductById(Long productId) {
        return client.target(String.format("http://localhost:8080/online-product-catalog/webApi/home/%d", productId) )
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<>(){});
    }
}
