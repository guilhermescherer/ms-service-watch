package com.guilhermescherer.msservicewatch.utils.api;

import io.restassured.http.ContentType;

import static com.guilhermescherer.msservicewatch.utils.api.ApiTestUtils.ApiEndpoint.SERVICE_ENDPOINT;
import static io.restassured.RestAssured.given;

public class ServiceEndpointApiTestUtils extends ApiTestUtils {

    public ServiceEndpointApiTestUtils(int port) {
        super(port);
    }

    @Override
    public io.restassured.response.ValidatableResponse post(String jsonBody) {
        return given()
                .baseUri(getBaseUri())
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(SERVICE_ENDPOINT.getEndpoint())
                .then();
    }

    @Override
    public io.restassured.response.ValidatableResponse delete(Integer id) {
        return given()
                .baseUri(getBaseUri())
                .when()
                .delete(SERVICE_ENDPOINT.getEndpoint() + "/" + id)
                .then();
    }

    @Override
    public io.restassured.response.ValidatableResponse put(Integer id, String jsonBody) {
        return given()
                .baseUri(getBaseUri())
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put(SERVICE_ENDPOINT.getEndpoint() + "/" + id)
                .then();
    }

    @Override
    public io.restassured.response.ValidatableResponse get(Integer id) {
        return given()
                .baseUri(getBaseUri())
                .when()
                .get(SERVICE_ENDPOINT.getEndpoint() + "/" + id)
                .then();
    }
}
