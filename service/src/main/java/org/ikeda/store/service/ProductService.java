package org.ikeda.store.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.ikeda.store.core.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Transactional
public class ProductService {
    public static final Logger logger =
            Logger.getLogger(ProductService.class.getCanonicalName());
    @PersistenceContext(unitName = "ProductDb")
    private EntityManager em;

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        cb = em.getCriteriaBuilder();
    }

    public List<Product> findAllProducts() {
        List<Product> data = new ArrayList<>();

        try {
            data = this.em.createNamedQuery("findAllProducts").getResultList();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error when finding all products", e);
        }
        return data;
    }

    public Product findProductById(Long productId) {
        logger.log(Level.INFO, "Finding product with id {0}", productId);
        return this.em.find(Product.class, productId);
    }

}
