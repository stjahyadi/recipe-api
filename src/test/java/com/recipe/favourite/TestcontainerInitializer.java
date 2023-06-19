package com.recipe.favourite;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestcontainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine")
            .withDatabaseName("demo")
            .withUsername("demo")
            .withPassword("demo");
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        postgres.start();
    }
}
