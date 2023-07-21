package org.ikeda.core.store;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Testcontainers
public class EntityManagerProducer {
    private final Logger log = Logger.getLogger(EntityManagerProducer.class.getCanonicalName());

    private static final String POSTGRES_DB = "product_db";
    private static final String POSTGRES_USER = "product_test_user";
    private static final String POSTGRES_PASSWORD = "letmein";

    private static final String POSTGRES_DRIVER_CLASS = "org.postgresql.Driver";

    @PersistenceContext(unitName="TestingDb")
    private EntityManager em;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName(POSTGRES_DB)
            .withPassword(POSTGRES_PASSWORD)
            .withUsername(POSTGRES_USER)
            .withExposedPorts(5432);

    @Produces
    @ApplicationScoped
    public EntityManager entityManager() throws Exception {
        log.info("Starting Database");
        postgres.start();
//        final String jdbcUrl =  String.format("jdbc:postgresql://%s:%d/%s", postgres.getHost(), postgres.getMappedPort(5432), POSTGRES_DB);

        log.log(Level.INFO, "Migrating database to {0}", postgres.getJdbcUrl());
        ResourceAccessor accessor = new ClassLoaderResourceAccessor();
        Database database = DatabaseFactory.getInstance().openDatabase(postgres.getJdbcUrl(), POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DRIVER_CLASS,
                null, null, null, accessor);;
        Liquibase liquibase = new Liquibase("db/database-changelog.yml", accessor, database);
        PrintWriter out = new PrintWriter(System.out);
        liquibase.update("", out);

        log.info("Database migrated!");
        log.info("Creating EntityManager...");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestingDb", Map.of(
                "jakarta.persistence.jdbc.driver", POSTGRES_DRIVER_CLASS,
                "jakarta.persistence.jdbc.url", postgres.getJdbcUrl(),
                "jakarta.persistence.jdbc.user", POSTGRES_USER,
                "jakarta.persistence.jdbc.password", POSTGRES_PASSWORD
        ));
        em = emf.createEntityManager();
        return em;
    }
}
