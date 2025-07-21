package com.guilhermescherer.msservicewatch.controller;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpStatus;
import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLog;
import com.guilhermescherer.msservicewatch.model.Status;
import com.guilhermescherer.msservicewatch.repository.ServiceEndpointRepository;
import com.guilhermescherer.msservicewatch.repository.ServiceStatusLogRepository;
import com.guilhermescherer.msservicewatch.utils.database.DatabaseTestUtils;
import com.guilhermescherer.msservicewatch.utils.api.ServiceEndpointApiTestUtils;
import com.guilhermescherer.msservicewatch.utils.api.ServiceStatusLogApiTestUtils;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ServiceStatusLogControllerTest {

    private static final String NOT_FOUND_ENTITY_ERROR = "Not found entity";

    @Container
    static PostgreSQLContainer<?> postgresContainer = DatabaseTestUtils.newPostgreSQLContainer();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private ServiceStatusLogRepository serviceStatusLogRepository;
    @Autowired
    private ServiceEndpointRepository serviceEndpointRepository;

    private ServiceStatusLogApiTestUtils serviceStatusLogApiTestUtils;
    private ServiceEndpointApiTestUtils serviceEndpointApiTestUtils;

    @PostConstruct
    public void setUp() {
        serviceStatusLogApiTestUtils = new ServiceStatusLogApiTestUtils(port);
        serviceEndpointApiTestUtils = new ServiceEndpointApiTestUtils(port);
    }

    @AfterEach
    void cleanDatabase() {
        serviceEndpointRepository.deleteAll();
        serviceStatusLogRepository.deleteAll();
    }

    @Nested
    @DisplayName("Get logs")
    class getLogs {

        @Test
        @DisplayName("Should return 200 and empty logs when request is valid and no logs exist")
        void shouldReturnEmptyLogsWhenNoLogsExistForServiceEndpoint() {
            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/health",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            Integer id = serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                    .path("id");

            serviceStatusLogApiTestUtils.get(id)
                    .statusCode(HttpStatus.SC_SUCCESS)
                    .body("content", empty())
                    .body("totalElements", equalTo(0))
                    .body("empty", equalTo(true));
        }

        @Test
        @DisplayName("Should return 200 and logs when request is valid and logs exist")
        void shouldReturnLogsWhenRequestIsValidAndLogsExist() {
            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/health",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            Integer id = serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                    .path("id");

            ServiceStatusLog serviceStatusLog = createExempleOfServiceStatusLog((long) id);

            serviceStatusLogApiTestUtils.get(id)
                    .statusCode(HttpStatus.SC_SUCCESS)
                    .body("totalElements", equalTo(1))
                    .body("empty", equalTo(false))
                    .body("content[0].status", equalTo("UP"))
                    .body("content[0].id", equalTo(serviceStatusLog.getId().intValue()))
                    .body("content[0].responseTimeMillis", equalTo(100));
        }

        @Test
        @DisplayName("Should return 404 when service endpoint is not found")
        void whenServiceEndpointNotFound_thenReturns404() {
            serviceStatusLogApiTestUtils.get(1)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("title", equalTo(NOT_FOUND_ENTITY_ERROR))
                    .body("status", equalTo(HttpStatus.SC_NOT_FOUND));
        }

        private ServiceStatusLog createExempleOfServiceStatusLog(long serviceEndpointId) {
            ServiceEndpoint serviceEndpoint = serviceEndpointRepository.findById(serviceEndpointId).orElseThrow();

            ServiceStatusLog serviceStatusLog = new ServiceStatusLog();

            serviceStatusLog.setCheckedAt(LocalDateTime.now());
            serviceStatusLog.setStatus(Status.UP);
            serviceStatusLog.setResponseTimeMillis(100L);
            serviceStatusLog.setService(serviceEndpoint);

            return serviceStatusLogRepository.save(serviceStatusLog);
        }
    }
}
