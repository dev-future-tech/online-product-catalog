package org.ikeda.store.web.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import org.ikeda.store.core.Product;
import org.ikeda.store.service.ProductService;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Model
public class ProductManager implements Serializable {

    private Product product;
    private List<Product> products;

    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());

    @PostConstruct
    private void init() {
        logger.info("new customer created");
        product = new Product();
        setProducts(service.findAllProducts());
    }

    @Inject
    ProductService service;

    @EJB
    ProductBean productBean;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
