package com.guilhermescherer.msservicewatch.service.core.impl;

import com.guilhermescherer.msservicewatch.data.ResponseData;
import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLogModel;
import com.guilhermescherer.msservicewatch.model.Status;
import com.guilhermescherer.msservicewatch.service.api.ServiceEndpointApiService;
import com.guilhermescherer.msservicewatch.service.database.ServiceEndpointDatabaseService;
import com.guilhermescherer.msservicewatch.service.database.ServiceStatusLogDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CheckServiceEndpointServiceImplTest {

    @Mock
    private ServiceEndpointApiService serviceEndpointApiService;

    @Mock
    private ServiceEndpointDatabaseService serviceEndpointDatabaseService;

    @Mock
    private ServiceStatusLogDatabaseService serviceStatusLogDatabaseService;

    @InjectMocks
    private CheckServiceEndpointServiceImpl checkServiceEndpointService;

    @Captor
    private ArgumentCaptor<ServiceStatusLogModel> statusLogCaptor;

    @Captor
    private ArgumentCaptor<ServiceEndpointModel> endpointCaptor;

    private ServiceEndpointModel serviceEndpoint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serviceEndpoint = new ServiceEndpointModel();
        serviceEndpoint.setUrl("http://example.com");
    }

    @Test
    @DisplayName("Check updates service endpoint and logs status when response is successful")
    void checkUpdatesServiceEndpointAndLogsStatusWhenResponseIsSuccessful() {
        ResponseData responseData = ResponseData.builder()
                .withHttpStatusCode(HttpStatusCode.valueOf(200))
                .withResponseTimeMillis(100L)
                .build();

        when(serviceEndpointApiService.callEndpoint(serviceEndpoint)).thenReturn(responseData);

        checkServiceEndpointService.check(serviceEndpoint);

        verify(serviceEndpointDatabaseService).save(endpointCaptor.capture());
        verify(serviceStatusLogDatabaseService).save(statusLogCaptor.capture());

        ServiceEndpointModel updatedEndpoint = endpointCaptor.getValue();
        assertEquals(Status.UP, updatedEndpoint.getLastStatus());
        assertNotNull(updatedEndpoint.getCheckedAt());

        ServiceStatusLogModel statusLog = statusLogCaptor.getValue();
        assertEquals(Status.UP, statusLog.getStatus());
        assertEquals(100L, statusLog.getResponseTimeMillis());
        assertNotNull(statusLog.getCheckedAt());
    }

    @Test
    @DisplayName("Check updates service endpoint and logs status when response is unsuccessful")
    void checkUpdatesServiceEndpointAndLogsStatusWhenResponseIsUnsuccessful() {
        ResponseData responseData = ResponseData.builder()
                .withHttpStatusCode(HttpStatusCode.valueOf(500))
                .withResponseTimeMillis(200L)
                .build();

        when(serviceEndpointApiService.callEndpoint(serviceEndpoint)).thenReturn(responseData);

        checkServiceEndpointService.check(serviceEndpoint);

        verify(serviceEndpointDatabaseService).save(endpointCaptor.capture());
        verify(serviceStatusLogDatabaseService).save(statusLogCaptor.capture());

        ServiceEndpointModel updatedEndpoint = endpointCaptor.getValue();
        assertEquals(Status.DOWN, updatedEndpoint.getLastStatus());
        assertNotNull(updatedEndpoint.getCheckedAt());

        ServiceStatusLogModel statusLog = statusLogCaptor.getValue();
        assertEquals(Status.DOWN, statusLog.getStatus());
        assertEquals(200L, statusLog.getResponseTimeMillis());
        assertNotNull(statusLog.getCheckedAt());
    }

    @Test
    @DisplayName("Check updates service endpoint and logs status when response is null")
    void checkUpdatesServiceEndpointAndLogsStatusWhenResponseIsNull() {
        ResponseData responseData = ResponseData.builder().build();

        when(serviceEndpointApiService.callEndpoint(serviceEndpoint)).thenReturn(responseData);

        checkServiceEndpointService.check(serviceEndpoint);

        verify(serviceEndpointDatabaseService).save(endpointCaptor.capture());
        verify(serviceStatusLogDatabaseService).save(statusLogCaptor.capture());

        ServiceEndpointModel updatedEndpoint = endpointCaptor.getValue();
        assertEquals(Status.UNKNOWN, updatedEndpoint.getLastStatus());
        assertNotNull(updatedEndpoint.getCheckedAt());

        ServiceStatusLogModel statusLog = statusLogCaptor.getValue();
        assertEquals(Status.UNKNOWN, statusLog.getStatus());
        assertNull(statusLog.getResponseTimeMillis());
        assertNotNull(statusLog.getCheckedAt());
    }
}
