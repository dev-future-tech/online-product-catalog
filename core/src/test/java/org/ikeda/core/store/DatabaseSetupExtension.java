package org.ikeda.core.store;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Qualifier;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.PrintWriter;
import java.util.logging.Logger;

@Testcontainers
public class DatabaseSetupExtension implements BeforeAllCallback, AfterAllCallback, ExtensionContext.Store.CloseableResource {

    private final Logger log = Logger.getLogger(DatabaseSetupExtension.class.getCanonicalName());

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

    public DatabaseSetupExtension() {
    }
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        log.info("Running beforeAll with DatabaseExtension...");
        postgres.start();

        ResourceAccessor accessor = new ClassLoaderResourceAccessor();
        Database database = DatabaseFactory.getInstance().openDatabase(postgres.getJdbcUrl(), POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DRIVER_CLASS,
                null, null, null, accessor);
        Liquibase liquibase = new Liquibase("db/database-changelog.yml", accessor, database);
        PrintWriter out = new PrintWriter(System.out);
        liquibase.update("", out);
        log.info("Database migrated!");
    }

    @Override
    public void close() throws Throwable {

    }

    @Produces
    @Named("containerJdbcUrl")
    public String getJdbcUrl() {
        return postgres.getJdbcUrl();
    }

    @Produces
    @Named("driverClass")
    public String getDriverClass() {
        return "org.postgresql.Driver";
    }

    @Produces
    @Named("username")
    public String getPostgresUser() {
        return "product_test_user";
    }

    @Produces
    @Named("userPassword")
    public String getPostgresPassword() {
        return "letmein";
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        postgres.stop();
    }
}
