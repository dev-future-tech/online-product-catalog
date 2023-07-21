package org.ikeda.core.store;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import liquibase.command.CommandScope;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
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

import java.util.logging.Logger;

@Testcontainers
public class DatabaseSetupExtension implements BeforeAllCallback, AfterAllCallback, ExtensionContext.Store.CloseableResource {

    private final Logger log = Logger.getLogger(DatabaseSetupExtension.class.getCanonicalName());

    private static final String POSTGRES_DB = "product_db";
    private static final String POSTGRES_USER = "product_test_user";
    private static final String POSTGRES_PASSWORD = "letmein";

    private static final String POSTGRES_DRIVER_CLASS = "org.postgresql.Driver";

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
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

        try {
            ResourceAccessor accessor = new ClassLoaderResourceAccessor();
            Database database = DatabaseFactory.getInstance().openDatabase(postgres.getJdbcUrl(), POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DRIVER_CLASS,
                    null, null, null, accessor);
            CommandScope update = new CommandScope("update")
                    .addArgumentValue("changeLogFile", "db/database-changelog.yml")
                            .addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, database);

                    update.execute();
            log.info("Database migrated!");
        } catch(Exception e) {
            log.severe("Error migrating database");
            e.printStackTrace();
        }

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
