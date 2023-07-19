package org.ikeda.core.store;


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
import org.ikeda.store.core.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartManagementTest {

    private final Logger log = Logger.getLogger(CartManagementTest.class.getCanonicalName());

    @Container
    GenericContainer postgres = new GenericContainer("postgres:15")
            .withExposedPorts(5432)
            .withEnv(Map.of(
                    "POSTGRES_PASSWORD", "letmein",
                    "POSTGRES_USER", "product_test_user",
                    "POSTGRES_DB", "product_db"
            ));



    @PersistenceContext
    private EntityManager entityManager;

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
        final String jdbcUrl =  String.format("jdbc:postgresql://%s:%d/product_db", postgres.getHost(), postgres.getMappedPort(5432));

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestingDb", Map.of(
                "jakarta.persistence.jdbc.driver", "org.postgresql.Driver",
                "jakarta.persistence.jdbc.url", jdbcUrl,
                "jakarta.persistence.jdbc.user", "product_test_user",
                "jakarta.persistence.jdbc.password", "letmein"
        ));
        entityManager = emf.createEntityManager();

        try {
            ResourceAccessor accessor = new ClassLoaderResourceAccessor();
            Database database = DatabaseFactory.getInstance().openDatabase(jdbcUrl, "product_test_user", "letmein", "org.postgresql.Driver",
                    null, null, null, accessor);;
            Liquibase liquibase = new Liquibase("db/database-changelog.yml", accessor, database);
            liquibase.update(new Contexts());

        } catch(LiquibaseException sqle) {
            log.log(Level.SEVERE, "Error preparing database: {0}", sqle);
        }

        List<Product> products = entityManager.createNamedQuery("findAllProducts", Product.class).getResultList();
        assertThat(products).isNotEmpty();
    }

}
