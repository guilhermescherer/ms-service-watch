package com.guilhermescherer.msservicewatch.controller;

import com.guilhermescherer.msservicewatch.utils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ServiceEndpointControllerTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = TestUtils.newPostgreSQLContainer();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @LocalServerPort
    private int port;

    @Nested
    @DisplayName("Test create ServiceEndpoint")
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

            given()
                    .baseUri(getBaseUri())
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/service-endpoint")
                    .then()
                    .statusCode(201)
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

            RestAssured.given()
                    .baseUri(getBaseUri())
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/service-endpoint")
                    .then()
                    .statusCode(400)
                    .body("title", equalTo("Validation error"))
                    .body("status", equalTo(400))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'name' }.message", equalTo("Name must not be blank"));
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

            RestAssured.given()
                    .baseUri(getBaseUri())
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/service-endpoint")
                    .then()
                    .statusCode(400)
                    .body("title", equalTo("Validation error"))
                    .body("status", equalTo(400))
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

            RestAssured.given()
                    .baseUri(getBaseUri())
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/service-endpoint")
                    .then()
                    .statusCode(400)
                    .body("title", equalTo("Validation error"))
                    .body("status", equalTo(400))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'url' }.message", equalTo("URL must start with http:// or https://"));
        }

        @Test
        @DisplayName("Should return 400 when url is greater than accepted")
        void whenUrlOutOfRange_thenReturns400() {
            String requestBody = """
                    {
                      "name": "Meu Serviço",
                      "url": "https://meuservico.com/healthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaahealthaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                      "active": true,
                      "checkInterval": 5
                    }
                    """;

            RestAssured.given()
                    .baseUri(getBaseUri())
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/service-endpoint")
                    .then()
                    .statusCode(400)
                    .body("title", equalTo("Validation error"))
                    .body("status", equalTo(400))
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

            RestAssured.given()
                    .baseUri(getBaseUri())
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/service-endpoint")
                    .then()
                    .statusCode(400)
                    .body("title", equalTo("Validation error"))
                    .body("status", equalTo(400))
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

            RestAssured.given()
                    .baseUri(getBaseUri())
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/service-endpoint")
                    .then()
                    .statusCode(400)
                    .body("title", equalTo("Validation error"))
                    .body("status", equalTo(400))
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

            RestAssured.given()
                    .baseUri(getBaseUri())
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/service-endpoint")
                    .then()
                    .statusCode(400)
                    .body("title", equalTo("Validation error"))
                    .body("status", equalTo(400))
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

            RestAssured.given()
                    .baseUri(getBaseUri())
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/service-endpoint")
                    .then()
                    .statusCode(400)
                    .body("title", equalTo("Validation error"))
                    .body("status", equalTo(400))
                    .body("fieldErrors", not(empty()))
                    .body("fieldErrors.find { it.field == 'checkInterval' }.message", equalTo("Check interval must not exceed 180 minutes"));
        }
    }

    private String getBaseUri() {
        return "http://localhost:" + port;
    }
}
