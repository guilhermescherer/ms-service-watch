package com.guilhermescherer.msservicewatch.utils;

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
