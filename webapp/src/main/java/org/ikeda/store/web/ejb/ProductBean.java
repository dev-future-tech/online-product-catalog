package org.ikeda.store.web.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import org.ikeda.store.core.Product;

import java.util.List;
import java.util.logging.Logger;

public class ProductBean {

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
        return client.target("http://localhost:8080/online-product-catalog/webApi/Product")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<Product>>() {
                });
    }
}
