package org.ikeda.core.store;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Qualifier;
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

    private String jdbcUrl;
    private String postgresDriver;

    @Inject
    @Named("username")
    private String username;

    private String password;

//    public EntityManagerProducer() {    }
    @Inject
    public EntityManagerProducer(@Named("containerJdbcUrl") String jdbcUrl, @Named("driverClass") String driverClass,
                                 @Named("userPassword")String password) {
        this.jdbcUrl = jdbcUrl;
        this.postgresDriver = driverClass;
        this.password = password;
    }

    @PersistenceContext(unitName="TestingDb")
    private EntityManager em;


    @Produces
    @ApplicationScoped
    public EntityManager entityManager() throws Exception {

        log.info("Creating EntityManager...");
        log.log(Level.INFO,"EntityManager jdbcUrl is {0}", jdbcUrl);
        log.log(Level.INFO,"EntityManager username is {0}", username);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestingDb", Map.of(
                "jakarta.persistence.jdbc.driver", postgresDriver,
                "jakarta.persistence.jdbc.url", jdbcUrl,
                "jakarta.persistence.jdbc.user", username,
                "jakarta.persistence.jdbc.password", password
        ));
        em = emf.createEntityManager();
        return em;
    }
}
