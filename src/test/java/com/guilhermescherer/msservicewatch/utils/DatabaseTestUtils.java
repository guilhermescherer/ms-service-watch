package com.guilhermescherer.msservicewatch.utils;

import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseTestUtils {

    public static PostgreSQLContainer<?> newPostgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
    }
}
