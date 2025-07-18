package com.guilhermescherer.msservicewatch.utils.api;

import lombok.Getter;

public abstract class ApiTestUtils {

    private static final String DEFAULT_BASE_URI = "http://localhost:";

    private final int port;

    protected ApiTestUtils(int port) {
        this.port = port;
    }

    protected String getBaseUri() {
        return DEFAULT_BASE_URI + port;
    }

    public io.restassured.response.ValidatableResponse post(String body) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public io.restassured.response.ValidatableResponse delete(Integer id) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public io.restassured.response.ValidatableResponse put(Integer id, String body) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public io.restassured.response.ValidatableResponse get(Integer id) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Getter
    protected enum ApiEndpoint {
        SERVICE_ENDPOINT("/service-endpoint"),
        LOGS("/logs");

        final String endpoint;

        ApiEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }
}
