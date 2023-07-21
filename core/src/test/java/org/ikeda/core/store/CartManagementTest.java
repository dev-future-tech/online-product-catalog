package org.ikeda.core.store;


import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.ikeda.store.core.CartItems;
import org.ikeda.store.core.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

//@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({DatabaseSetupExtension.class, CDIExtension.class})
class CartManagementTest {

    @Inject
    EntityManager entityManager;

    private final Logger log = Logger.getLogger(CartManagementTest.class.getCanonicalName());

    @Test
    @DisplayName("Test container is running")
    @Order(1)
    void testContainerIsRunning() {
        assertThat(true).isTrue();
    }

    @Test
    @DisplayName("test that cart items can be retrieved")
    @Order(2)
    void testCartItems() {
        List<Product> products = entityManager.createNamedQuery("findAllProducts", Product.class).getResultList();
        assertThat(products).isNotEmpty();
    }

    @Test
    @Order(3)
    public void testCreateCartItem() {

        String cartId = UUID.randomUUID().toString();
        Long customerId = 456789L;

        CartItems items = new CartItems();
        items.setCartId(cartId);
        items.setCustomerId(customerId);

        Product item1 = entityManager.find(Product.class, 15L);
        Product item2 = entityManager.find(Product.class, 16L);
        items.setItems(List.of(item1, item2));

        entityManager.getTransaction().begin();
        entityManager.persist(items);
        entityManager.getTransaction().commit();

        assertThat(items.getCartId()).isNotNull();

        List<CartItems> results = entityManager.createNamedQuery("findAllCustomerCartItems", CartItems.class).setParameter("customerId", customerId ).getResultList();
        assertThat(results).isNotNull().isNotEmpty();
        log.log(Level.INFO, "Total results is: {0}", results.size());
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getItems().size()).isEqualTo(2);
        log.log(Level.INFO, "Total cart items is {0}", results.get(0).getItems().size());
    }
}
