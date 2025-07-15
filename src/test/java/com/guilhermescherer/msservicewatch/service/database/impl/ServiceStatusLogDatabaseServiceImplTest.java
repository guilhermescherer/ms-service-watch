package com.guilhermescherer.msservicewatch.service.database.impl;

import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLog;
import com.guilhermescherer.msservicewatch.model.Status;
import com.guilhermescherer.msservicewatch.repository.ServiceEndpointRepository;
import com.guilhermescherer.msservicewatch.repository.ServiceStatusLogRepository;
import com.guilhermescherer.msservicewatch.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
public class ServiceStatusLogDatabaseServiceImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = TestUtils.newContainer();

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private ServiceStatusLogRepository serviceStatusLogRepository;
    @Autowired
    private ServiceEndpointRepository serviceEndpointRepository;
    @Autowired
    private ServiceStatusLogDatabaseServiceImpl serviceStatusLogDatabaseService;

    @Test
    @DisplayName("Should save ServiceStatusLog entity")
    void shouldSaveServiceStatusLog() {
        ServiceEndpoint endpoint = new ServiceEndpoint();
        endpoint.setName("Test Service");
        endpoint.setUrl("http://localhost");
        endpoint.setActive(true);
        endpoint.setCheckInterval(60);
        ServiceEndpoint savedEndpoint = serviceEndpointRepository.save(endpoint);

        ServiceStatusLog log = new ServiceStatusLog();
        log.setService(savedEndpoint);
        log.setStatus(Status.UP);
        log.setResponseTimeMillis(100L);
        log.setCheckedAt(LocalDateTime.now());

        serviceStatusLogDatabaseService.save(log);

        assertNotNull(log.getId());
        assertTrue(serviceStatusLogRepository.findById(log.getId()).isPresent());
    }

    @Test
    @DisplayName("Should return paged logs by ServiceEndpoint")
    void shouldReturnPagedLogsByServiceEndpoint() {
        ServiceEndpoint endpoint = new ServiceEndpoint();
        endpoint.setName("Test Service 2");
        endpoint.setUrl("http://localhost/2");
        endpoint.setActive(true);
        endpoint.setCheckInterval(60);
        ServiceEndpoint savedEndpoint = serviceEndpointRepository.save(endpoint);

        for (int i = 0; i < 3; i++) {
            ServiceStatusLog log = new ServiceStatusLog();
            log.setService(savedEndpoint);
            log.setStatus(Status.UP);
            log.setResponseTimeMillis(100L + i);
            log.setCheckedAt(LocalDateTime.now());
            serviceStatusLogRepository.save(log);
        }

        Pageable pageable = PageRequest.of(0, 2);

        Page<ServiceStatusLog> page = serviceStatusLogDatabaseService.getLogsByServiceEndpoint(savedEndpoint, pageable);

        assertEquals(2, page.getContent().size());
        assertEquals(3, page.getTotalElements());
        assertEquals(0, page.getNumber());
    }
}
