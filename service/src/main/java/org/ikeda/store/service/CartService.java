package org.ikeda.store.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Transactional
public class CartService {

    public static final Logger logger = Logger.getLogger(CartService.class.getCanonicalName());

    @PersistenceContext(unitName = "ProductDb")
    private EntityManager em;

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        cb = em.getCriteriaBuilder();
    }

    public String addToCart(Long productId, Integer qty, String cartId) {
        logger.log(Level.FINE, "Adding {0} products of {1} to card {2}", new Object[]{qty, productId, cartId});
        return null;
    }
}
