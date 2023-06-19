package com.recipe.favourite;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;
import java.util.List;

public class TestcontainerExtension implements BeforeAllCallback, AfterAllCallback {

    private static PostgreSQLContainer<?> postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:14-alpine")
                .withDatabaseName("demo")
                .withUsername("demo")
                .withPassword("demo");

        postgresContainer.setPortBindings(List.of("5432:5432"));
        postgresContainer.start();
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.setProperty("TESTCONTAINERS_RYUK_DISABLED", "true");
        // Perform any necessary setup before the tests start
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // Perform any necessary cleanup after the tests complete
        if (postgresContainer != null && postgresContainer.isRunning()) {
            postgresContainer.stop();
        }
    }
}
