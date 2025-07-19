package com.guilhermescherer.msservicewatch.utils.api;

import static com.guilhermescherer.msservicewatch.utils.api.ApiTestUtils.ApiEndpoint.LOGS;
import static com.guilhermescherer.msservicewatch.utils.api.ApiTestUtils.ApiEndpoint.SERVICE_ENDPOINT;
import static io.restassured.RestAssured.given;

public class ServiceStatusLogApiTestUtils extends ApiTestUtils {

    public ServiceStatusLogApiTestUtils(int port) {
        super(port);
    }

    @Override
    public io.restassured.response.ValidatableResponse get(Integer serviceEndpointId) {
        return given()
                .baseUri(getBaseUri())
                .when()
                .get(SERVICE_ENDPOINT.getEndpoint() + "/" + serviceEndpointId + LOGS.getEndpoint())
                .then();
    }
}
