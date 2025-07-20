package com.guilhermescherer.msservicewatch.facade.impl;

import com.guilhermescherer.msservicewatch.converter.response.ServiceLogStatusResponseConverter;
import com.guilhermescherer.msservicewatch.dto.response.ServiceStatusLogResponse;
import com.guilhermescherer.msservicewatch.exception.NotFoundException;
import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLog;
import com.guilhermescherer.msservicewatch.service.database.ServiceEndpointDatabaseService;
import com.guilhermescherer.msservicewatch.service.database.ServiceStatusLogDatabaseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

class ServiceStatusLogFacadeImplTest {

    @Mock
    private ServiceEndpointDatabaseService serviceEndpointDatabaseService;
    @Mock
    private ServiceStatusLogDatabaseService serviceStatusLogDatabaseService;
    @Mock
    private ServiceLogStatusResponseConverter serviceLogStatusResponseConverter;
    @Mock
    private ServiceEndpoint serviceEndpoint;
    @Mock
    private Pageable pageable;
    @Mock
    private Page<ServiceStatusLogResponse> pageResponse;
    @Mock
    private Page<ServiceStatusLog> serviceStatusLogs;
    @InjectMocks
    private ServiceStatusLogFacadeImpl facade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return paged logs for existing service endpoint")
    void shouldReturnPagedLogsForExistingServiceEndpoint() {
        Long serviceEndpointId = 1L;
        Mockito.when(serviceEndpointDatabaseService.getById(serviceEndpointId))
                .thenReturn(Optional.of(serviceEndpoint));
        Mockito.when(serviceStatusLogDatabaseService.getLogsByServiceEndpoint(serviceEndpoint, pageable))
                .thenReturn(serviceStatusLogs);
        Mockito.when(serviceStatusLogs.map(Mockito.<java.util.function.Function<ServiceStatusLog, ServiceStatusLogResponse>>any()))
                .thenReturn(pageResponse);

        Page<ServiceStatusLogResponse> result = facade.getLogsByServiceEndpointId(serviceEndpointId, pageable);

        Assertions.assertEquals(pageResponse, result);
    }

    @Test
    @DisplayName("Should throw NotFoundException when service endpoint does not exist")
    void shouldThrowNotFoundExceptionWhenServiceEndpointDoesNotExist() {
        Long serviceEndpointId = 2L;
        Mockito.when(serviceEndpointDatabaseService.getById(serviceEndpointId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                NotFoundException.class,
                () -> facade.getLogsByServiceEndpointId(serviceEndpointId, pageable)
        );
    }
}
