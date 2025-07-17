package com.guilhermescherer.msservicewatch.utils;

import io.restassured.http.ContentType;

import static com.guilhermescherer.msservicewatch.utils.ApiTestUtils.ApiEndpoint.SERVICE_ENDPOINT;
import static io.restassured.RestAssured.given;

public class ServiceStatusApiTestUtils extends ApiTestUtils {

    public ServiceStatusApiTestUtils(int port) {
        super(port);
    }

    public io.restassured.response.ValidatableResponse getServiceEndpoint(Integer id) {
        return given()
                .baseUri(getBaseUri())
                .when()
                .get(SERVICE_ENDPOINT.getEndpoint() + "/" + id)
                .then();
    }

    public io.restassured.response.ValidatableResponse putServiceEndpoint(Integer id, String jsonBody) {
        return given()
                .baseUri(getBaseUri())
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put(SERVICE_ENDPOINT.getEndpoint() + "/" + id)
                .then();
    }

    public io.restassured.response.ValidatableResponse deleteServiceEndpoint(Integer id) {
        return given()
                .baseUri(getBaseUri())
                .when()
                .delete(SERVICE_ENDPOINT.getEndpoint() + "/" + id)
                .then();
    }

    public io.restassured.response.ValidatableResponse postServiceEndpoint(String jsonBody) {
        return given()
                .baseUri(getBaseUri())
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(SERVICE_ENDPOINT.getEndpoint())
                .then();
    }
}
