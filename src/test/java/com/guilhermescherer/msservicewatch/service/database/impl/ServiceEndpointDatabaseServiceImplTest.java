package com.guilhermescherer.msservicewatch.service.database.impl;

import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import com.guilhermescherer.msservicewatch.repository.ServiceEndpointRepository;
import com.guilhermescherer.msservicewatch.service.database.ServiceEndpointDatabaseService;
import com.guilhermescherer.msservicewatch.utils.DatabaseTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceEndpointDatabaseServiceImplTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = DatabaseTestUtils.newPostgreSQLContainer();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private ServiceEndpointRepository repository;
    @Autowired
    private ServiceEndpointDatabaseService service;

    private ServiceEndpoint endpoint;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        endpoint = new ServiceEndpoint();
        endpoint.setName("My API");
        endpoint.setUrl("https://api.test.com");
        endpoint.setActive(true);
        endpoint.setCheckInterval(60);
    }

    @Test
    @DisplayName("Should save and retrieve service endpoint")
    void shouldSaveAndRetrieveServiceEndpoint() {
        ServiceEndpoint saved = service.save(endpoint);

        Optional<ServiceEndpoint> found = service.getById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("My API", found.get().getName());
        assertEquals("https://api.test.com", found.get().getUrl());
    }

    @Test
    @DisplayName("Should return all endpoints")
    void shouldReturnAllEndpoints() {
        service.save(endpoint);

        ServiceEndpoint serviceEndpoint = new ServiceEndpoint();
        serviceEndpoint.setName("Other API");
        serviceEndpoint.setUrl("http://example.com");
        serviceEndpoint.setActive(true);
        serviceEndpoint.setCheckInterval(30);
        service.save(serviceEndpoint);

        List<ServiceEndpoint> all = service.getAll();

        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("Should delete endpoint by id")
    void shouldDeleteById() {
        ServiceEndpoint saved = service.save(endpoint);

        service.delete(saved.getId());

        assertFalse(service.getById(saved.getId()).isPresent());
    }
}
