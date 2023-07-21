package org.ikeda.core.store;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.testcontainers.junit.jupiter.Testcontainers;

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

    @Inject
    public EntityManagerProducer(@Named("containerJdbcUrl") String jdbcUrl, @Named("driverClass") String driverClass,
                                 @Named("userPassword")String password) {
        this.jdbcUrl = jdbcUrl;
        this.postgresDriver = driverClass;
        this.password = password;
    }


    @Produces
    @ApplicationScoped
    public EntityManagerFactory entityManager() throws Exception {
        log.info("Creating EntityManager...");
        log.log(Level.INFO,"EntityManager jdbcUrl is {0}", jdbcUrl);
        log.log(Level.INFO,"EntityManager username is {0}", username);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestingDb", Map.of(
                "jakarta.persistence.jdbc.driver", postgresDriver,
                "jakarta.persistence.jdbc.url", jdbcUrl,
                "jakarta.persistence.jdbc.user", username,
                "jakarta.persistence.jdbc.password", password,
                "eclipselink.logging.level.sql", "FINE",
                "eclipselink.logging.parameters", "true"
        ));
        return emf;
    }
}
