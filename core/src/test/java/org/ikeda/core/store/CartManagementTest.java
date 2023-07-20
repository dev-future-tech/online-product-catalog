package org.ikeda.core.store;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CartManagementTest {

    private final Logger log = Logger.getLogger(CartManagementTest.class.getCanonicalName());

    private static final String POSTGRES_DB = "product_db";
    private static final String POSTGRES_USER = "product_test_user";
    private static final String POSTGRES_PASSWORD = "letmein";

    private static final String POSTGRES_DRIVER_CLASS = "org.postgresql.Driver";

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName(POSTGRES_DB)
            .withPassword(POSTGRES_PASSWORD)
            .withUsername(POSTGRES_USER)
            .withExposedPorts(5432);


    @Test
    @DisplayName("Test container is running")
    @Order(1)
    void testContainerIsRunning() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @DisplayName("test that cart items can be retrieved")
    @Order(2)
    void testCartItems() {
        final String jdbcUrl =  String.format("jdbc:postgresql://%s:%d/%s", postgres.getHost(), postgres.getMappedPort(5432), POSTGRES_DB);

        EntityManagerFactory emf = getEntityManagerFactory(jdbcUrl);
        EntityManager entityManager = emf.createEntityManager();

        try {
            ResourceAccessor accessor = new ClassLoaderResourceAccessor();
            Database database = DatabaseFactory.getInstance().openDatabase(jdbcUrl, POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DRIVER_CLASS,
                    null, null, null, accessor);;
            Liquibase liquibase = new Liquibase("db/database-changelog.yml", accessor, database);
            liquibase.update(new Contexts());

        } catch(LiquibaseException sqle) {
            log.log(Level.SEVERE, "Error preparing database: {0}", sqle);
        }

        List<Product> products = entityManager.createNamedQuery("findAllProducts", Product.class).getResultList();
        assertThat(products).isNotEmpty();
    }

    @Test
    @Order(3)
    public void testCreateCartItem() {
        final String jdbcUrl =  String.format("jdbc:postgresql://%s:%d/%s", postgres.getHost(), postgres.getMappedPort(5432), POSTGRES_DB);

        EntityManagerFactory emf = getEntityManagerFactory(jdbcUrl);
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

    private EntityManagerFactory getEntityManagerFactory(String jdbcUrl) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestingDb", Map.of(
                "jakarta.persistence.jdbc.driver", POSTGRES_DRIVER_CLASS,
                "jakarta.persistence.jdbc.url", jdbcUrl,
                "jakarta.persistence.jdbc.user", POSTGRES_USER,
                "jakarta.persistence.jdbc.password", POSTGRES_PASSWORD
        ));

        return emf;
    }

}
