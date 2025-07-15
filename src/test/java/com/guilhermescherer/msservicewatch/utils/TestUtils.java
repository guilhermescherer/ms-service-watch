package com.guilhermescherer.msservicewatch.utils;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestUtils {

    public static PostgreSQLContainer<?> newContainer() {
        return new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
    }
}
