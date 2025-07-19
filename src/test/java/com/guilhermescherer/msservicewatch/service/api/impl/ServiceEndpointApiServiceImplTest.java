package com.guilhermescherer.msservicewatch.service.api.impl;

import com.guilhermescherer.msservicewatch.data.ResponseData;
import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceEndpointApiServiceImplTest {

    private static MockWebServer mockWebServer;
    private ServiceEndpointApiServiceImpl service;
    private ServiceEndpoint serviceEndpoint;

    @BeforeAll
    static void startServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void shutdownServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        service = new ServiceEndpointApiServiceImpl(webClient);
        serviceEndpoint = new ServiceEndpoint();
        serviceEndpoint.setUrl(mockWebServer.url("/test").toString());
    }

    @Test
    @DisplayName("Call endpoint returns response data for HTTP 200 OK")
    void callEndpointReturnsResponseDataForHttp200() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        ResponseData responseData = service.callEndpoint(serviceEndpoint);

        assertNotNull(responseData);
        assertEquals(HttpStatus.OK, responseData.getHttpStatusCode());
        assertTrue(responseData.getResponseTimeMillis() >= 0);
    }

    @Test
    @DisplayName("Call endpoint returns response data for HTTP 404 Not Found")
    void callEndpointReturnsResponseDataForHttp404() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        ResponseData responseData = service.callEndpoint(serviceEndpoint);

        assertNotNull(responseData);
        assertEquals(HttpStatus.NOT_FOUND, responseData.getHttpStatusCode());
        assertTrue(responseData.getResponseTimeMillis() >= 0);
    }

    @Test
    @DisplayName("Call endpoint returns unexpected exception")
    void callEndpointReturnsUnexpectedException() throws IOException {
        MockWebServer isolatedServer = new MockWebServer();
        isolatedServer.start();
        isolatedServer.shutdown();

        WebClient isolatedWebClient = WebClient.builder()
                .baseUrl(isolatedServer.url("/").toString())
                .build();

        ServiceEndpointApiServiceImpl isolatedService = new ServiceEndpointApiServiceImpl(isolatedWebClient);
        ServiceEndpoint isolatedServiceEndpoint = new ServiceEndpoint();
        isolatedServiceEndpoint.setUrl(isolatedServer.url("/test").toString());

        ResponseData response = isolatedService.callEndpoint(isolatedServiceEndpoint);

        assertNotNull(response);
        assertNull(response.getHttpStatusCode());
        assertNull(response.getResponseTimeMillis());
    }
}
