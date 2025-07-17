package com.guilhermescherer.msservicewatch.utils;

import static com.guilhermescherer.msservicewatch.utils.ApiTestUtils.ApiEndpoint.LOGS;
import static com.guilhermescherer.msservicewatch.utils.ApiTestUtils.ApiEndpoint.SERVICE_ENDPOINT;
import static io.restassured.RestAssured.given;

public class ServiceStatusLogApiTestUtils extends ApiTestUtils {

    public ServiceStatusLogApiTestUtils(int port) {
        super(port);
    }

    public io.restassured.response.ValidatableResponse getServiceEndpointLogs(Integer serviceEndpointId) {
        return given()
                .baseUri(getBaseUri())
                .when()
                .get(SERVICE_ENDPOINT.getEndpoint() + "/" + serviceEndpointId + LOGS.getEndpoint())
                .then();
    }
}
