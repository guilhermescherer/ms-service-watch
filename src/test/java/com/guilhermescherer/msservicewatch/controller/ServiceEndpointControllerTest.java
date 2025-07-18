package com.guilhermescherer.msservicewatch.controller;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpStatus;
import com.guilhermescherer.msservicewatch.repository.ServiceEndpointRepository;
import com.guilhermescherer.msservicewatch.utils.api.ServiceEndpointApiTestUtils;
import com.guilhermescherer.msservicewatch.utils.database.DatabaseTestUtils;
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

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.withArgs;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ServiceEndpointControllerTest {

    private static final String VALIDATION_ERROR = "Validation error";
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
    private ServiceEndpointRepository serviceEndpointRepository;

    private ServiceEndpointApiTestUtils serviceEndpointApiTestUtils;

    @PostConstruct
    public void setUp() {
        serviceEndpointApiTestUtils = new ServiceEndpointApiTestUtils(port);
    }

    @AfterEach
    void cleanDatabase() {
        serviceEndpointRepository.deleteAll();
    }

    @Nested
    @DisplayName("Test create service endpoint")
    class create {

        @Test
        @DisplayName("Should return 201 when request is valid")
        void shouldCreateServiceEndpoint() {
            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/health",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_CREATED)
                    .body("id", notNullValue())
                    .body("name", equalTo("Meu Serviço"))
                    .body("url", equalTo("https://meuservico.com/health"))
                    .body("active", equalTo(true))
                    .body("checkInterval", equalTo(5))
                    .body("lastStatus", nullValue())
                    .body("lastCheckTime", nullValue());
        }

        @Test
        @DisplayName("Should return 400 when name is blank")
        void whenNameBlank_thenReturns400() {
            String requestBody = """
                    {
                      "url": "https://meuservico.com/health",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'name' }.message", equalTo("Name must not be blank"));
        }

        @Test
        @DisplayName("Should return 400 when name is greater than accepted")
        void whenNameOutOfRange_thenReturns400() {
            String name = "Name" + "e".repeat(300);

            String requestBody = String.format("""
                    {
                        "name": "%s",
                      "url": "https://meuservico.com/health",
                      "active": true,
                      "checkInterval": 5
                    }
                    """, name);

            serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'name' }.message", equalTo("Name must be at most 255 characters"));
        }

        @Test
        @DisplayName("Should return 400 when url is blank")
        void whenUrlBlank_thenReturns400() {
            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'url' }.message", equalTo("URL must not be blank"));
        }

        @Test
        @DisplayName("Should return 400 when url is invalid")
        void whenUrlInvalid_thenReturns400() {
            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "something.com.br",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'url' }.message", equalTo("URL must start with http:// or https://"));
        }

        @Test
        @DisplayName("Should return 400 when url is greater than accepted")
        void whenUrlOutOfRange_thenReturns400() {
            String url = "https://meuservico.com/" + "a".repeat(2050);
            String requestBody = java.lang.String.format("""
                    {
                      "name": "Meu Serviço",
                      "url": "%s",
                      "active": true,
                      "checkInterval": 5
                    }
                    """, url);

            serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'url' }.message", equalTo("URL must be at most 2048 characters"));
        }

        @Test
        @DisplayName("Should return 400 when active is null")
        void whenActiveNull_thenReturns400() {
            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/health",
                      "checkInterval": 5
                    }
                    """;

            serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'active' }.message", equalTo("Active must not be null"));
        }

        @Test
        @DisplayName("Should return 400 when check interval is null")
        void whenCheckIntervalNull_thenReturns400() {
            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/health",
                      "active": true
                    }
                    """;

            serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'checkInterval' }.message", equalTo("Check interval must not be null"));
        }

        @Test
        @DisplayName("Should return 400 when check interval is less than accepted")
        void whenCheckIntervalLessThan_thenReturns400() {
            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/health",
                      "active": true,
                      "checkInterval": 0
                    }
                    """;

            serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'checkInterval' }.message", equalTo("Check interval must be at least 1 minute"));
        }

        @Test
        @DisplayName("Should return 400 when check interval is greater than accepted")
        void whenCheckIntervalGreaterThan_thenReturns400() {
            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/health",
                      "active": true,
                      "checkInterval": 185
                    }
                    """;

            serviceEndpointApiTestUtils.post(requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'checkInterval' }.message", equalTo("Check interval must not exceed 180 minutes"));
        }
    }

    @Nested
    @DisplayName("Test get service endpoint by id")
    class getById {

        @Test
        @DisplayName("Should return 200 when request is valid")
        void shouldReturnServiceEndpoint() {
            Integer id = createExampleOfServiceEndpoint();

            serviceEndpointApiTestUtils.get(id)
                    .statusCode(HttpStatus.SC_SUCCESS)
                    .body("id", equalTo(id))
                    .body("name", equalTo("Exemplo"))
                    .body("url", equalTo("https://exemplo.com/health"))
                    .body("active", equalTo(true))
                    .body("checkInterval", equalTo(5))
                    .body("lastStatus", nullValue())
                    .body("lastCheckTime", nullValue());
        }

        @Test
        @DisplayName("Should return 404 when service endpoint is not found")
        void whenServiceEndpointNotFound_thenReturns404() {
            serviceEndpointApiTestUtils.get(1)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("title", equalTo(NOT_FOUND_ENTITY_ERROR))
                    .body("status", equalTo(HttpStatus.SC_NOT_FOUND));
        }
    }

    @Nested
    @DisplayName("Test get all service endpoints")
    class getAll {

        @Test
        @DisplayName("Should return 200 with all service endpoints")
        void shouldReturnAllServiceEndpoints() {
            String firstRequest = """
                    {
                      "name": "Serviço A",
                      "url": "https://servicoa.com/health",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            String secondRequest = """
                    {
                      "name": "Serviço B",
                      "url": "https://servicob.com/health",
                      "active": false,
                      "checkInterval": 10
                    }
                    """;

            Integer firstId = serviceEndpointApiTestUtils.post(firstRequest)
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                    .path("id");

            Integer secondId = serviceEndpointApiTestUtils.post(secondRequest)
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                    .path("id");

            given()
                    .baseUri(getBaseUri())
                    .when()
                    .get("/service-endpoint")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("", not(empty()))
                    .body("size()", equalTo(2))
                    .body("find { it.id == %d }.name", withArgs(firstId), equalTo("Serviço A"))
                    .body("find { it.id == %d }.name", withArgs(secondId), equalTo("Serviço B"));
        }

        @Test
        @DisplayName("Should return 200 with empty list when no service endpoint exists")
        void shouldReturnEmptyListWhenNoneExist() {
            given()
                    .baseUri(getBaseUri())
                    .when()
                    .get("/service-endpoint")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("", empty());
        }
    }

    @Nested
    @DisplayName("Test update service endpoint")
    class update {

        @Test
        @DisplayName("Should return 200 when request is valid")
        void shouldUpdateServiceEndpoint() {
            Integer id = createExampleOfServiceEndpoint();

            String updated = """
                    {
                      "name": "Serviço Atualizado",
                      "url": "https://atualizado.com/health",
                      "active": false,
                      "checkInterval": 15
                    }
                    """;

            serviceEndpointApiTestUtils.put(id, updated)
                    .statusCode(HttpStatus.SC_OK)
                    .body("id", equalTo(id))
                    .body("name", equalTo("Serviço Atualizado"))
                    .body("url", equalTo("https://atualizado.com/health"))
                    .body("active", equalTo(false))
                    .body("checkInterval", equalTo(15));
        }

        @Test
        @DisplayName("Should return 400 when name is blank")
        void whenNameBlank_thenReturns400() {
            Integer id = createExampleOfServiceEndpoint();

            String body = """
                    {
                      "url": "https://meuservico.com/health",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            serviceEndpointApiTestUtils.put(id, body)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'name' }.message", equalTo("Name must not be blank"));
        }

        @Test
        @DisplayName("Should return 400 when name is greater than accepted")
        void whenNameOutOfRange_thenReturns400() {
            Integer id = createExampleOfServiceEndpoint();

            String name = "Name" + "e".repeat(300);
            String body = String.format("""
                    {
                      "name": "%s",
                      "url": "https://meuservico.com/health",
                      "active": true,
                      "checkInterval": 5
                    }
                    """, name);

            serviceEndpointApiTestUtils.put(id, body)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'name' }.message", equalTo("Name must be at most 255 characters"));
        }

        @Test
        @DisplayName("Should return 400 when url is blank")
        void whenUrlBlank_thenReturns400() {
            Integer id = createExampleOfServiceEndpoint();

            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            serviceEndpointApiTestUtils.put(id, requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'url' }.message", equalTo("URL must not be blank"));
        }

        @Test
        @DisplayName("Should return 400 when url is invalid")
        void whenUrlInvalid_thenReturns400() {
            Integer id = createExampleOfServiceEndpoint();

            String requestBody = """
                    {
                      "name": "Serviço",
                      "url": "semhttp.com",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            serviceEndpointApiTestUtils.put(id, requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'url' }.message", equalTo("URL must start with http:// or https://"));
        }

        @Test
        @DisplayName("Should return 400 when url is greater than accepted")
        void whenUrlOutOfRange_thenReturns400() {
            Integer id = createExampleOfServiceEndpoint();

            String url = "https://meuservico.com/" + "a".repeat(2050);
            String requestBody = java.lang.String.format("""
                    {
                      "name": "Meu Serviço",
                      "url": "%s",
                      "active": true,
                      "checkInterval": 5
                    }
                    """, url);

            serviceEndpointApiTestUtils.put(id, requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'url' }.message", equalTo("URL must be at most 2048 characters"));
        }

        @Test
        @DisplayName("Should return 400 when active is null")
        void whenActiveNull_thenReturns400() {
            Integer id = createExampleOfServiceEndpoint();

            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/health",
                      "checkInterval": 5
                    }
                    """;

            serviceEndpointApiTestUtils.put(id, requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'active' }.message", equalTo("Active must not be null"));
        }

        @Test
        @DisplayName("Should return 400 when check interval is null")
        void whenCheckIntervalNull_thenReturns400() {
            Integer id = createExampleOfServiceEndpoint();

            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/health",
                      "active": true
                    }
                    """;

            serviceEndpointApiTestUtils.put(id, requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'checkInterval' }.message", equalTo("Check interval must not be null"));
        }


        @Test
        @DisplayName("Should return 400 when checkInterval is less than allowed")
        void whenCheckIntervalTooSmall_thenReturns400() {
            Integer id = createExampleOfServiceEndpoint();

            String requestBody = """
                    {
                      "name": "Serviço",
                      "url": "https://valido.com",
                      "active": true,
                      "checkInterval": 0
                    }
                    """;

            serviceEndpointApiTestUtils.put(id, requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'checkInterval' }.message", equalTo("Check interval must be at least 1 minute"));
        }

        @Test
        @DisplayName("Should return 400 when check interval is greater than accepted")
        void whenCheckIntervalGreaterThan_thenReturns400() {
            Integer id = createExampleOfServiceEndpoint();

            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/health",
                      "active": true,
                      "checkInterval": 185
                    }
                    """;

            serviceEndpointApiTestUtils.put(id, requestBody)
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .body("title", equalTo(VALIDATION_ERROR))
                    .body("status", equalTo(HttpStatus.SC_BAD_REQUEST))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'checkInterval' }.message", equalTo("Check interval must not exceed 180 minutes"));
        }

        @Test
        @DisplayName("Should return 404 when ID does not exist")
        void whenIdDoesNotExist_thenReturns404() {
            String body = """
                    {
                      "name": "Novo",
                      "url": "https://valido.com",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            serviceEndpointApiTestUtils.put(99999, body)
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .body("status", equalTo(HttpStatus.SC_NOT_FOUND))
                    .body("title", equalTo(NOT_FOUND_ENTITY_ERROR));
        }
    }

    @Nested
    @DisplayName("Test delete service endpoint")
    class delete {

        @Test
        @DisplayName("Should return 200 when request is valid")
        void shoulddelete() {
            Integer id = createExampleOfServiceEndpoint();

            serviceEndpointApiTestUtils.delete(id).statusCode(HttpStatus.SC_NO_CONTENT);

            serviceEndpointApiTestUtils.get(id).statusCode(HttpStatus.SC_NOT_FOUND);
        }

        @Test
        @DisplayName("Should return 200 when delete a non-existent service endpoint")
        void whenServiceEndpointNotFound_thenReturns404() {
            Integer id = 9999;

            serviceEndpointApiTestUtils.delete(id).statusCode(HttpStatus.SC_NO_CONTENT);

            serviceEndpointApiTestUtils.get(id).statusCode(HttpStatus.SC_NOT_FOUND);
        }
    }

    private Integer createExampleOfServiceEndpoint() {
        String request = """
                {
                  "name": "Exemplo",
                  "url": "https://exemplo.com/health",
                  "active": true,
                  "checkInterval": 5
                }
                """;

        return serviceEndpointApiTestUtils.post(request)
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("id");
    }

    private String getBaseUri() {
        return "http://localhost:" + port;
    }
}
