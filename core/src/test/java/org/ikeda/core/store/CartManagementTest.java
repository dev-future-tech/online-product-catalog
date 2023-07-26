package org.ikeda.core.store;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.ikeda.store.core.CartItems;
import org.ikeda.store.core.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({DatabaseSetupExtension.class, CDIExtension.class})
class CartManagementTest {

    @PersistenceUnit
    EntityManagerFactory emf;

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
        EntityManager entityManager = emf.createEntityManager();
        log.info(emf.getProperties().toString());
        entityManager.getTransaction().begin();
        List<Product> products = entityManager.createNamedQuery("findAllProducts", Product.class).getResultList();
        entityManager.getTransaction().commit();
        assertThat(products).isNotEmpty();
    }

    @Test
    @Order(3)
    public void testCreateCartItem() {
        EntityManager entityManager = emf.createEntityManager();

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
