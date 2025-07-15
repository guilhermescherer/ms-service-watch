package com.guilhermescherer.msservicewatch.scheduler.impl;

import com.guilhermescherer.msservicewatch.facade.ServiceEndpointFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServiceEndpointSchedulerImplTest {

    @Mock
    private ServiceEndpointFacade serviceEndpointFacade;
    @InjectMocks
    private ServiceEndpointSchedulerImpl serviceEndpointScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Perform calls verifyServiceEndpoints on facade")
    void performCallsVerifyServiceEndpointsOnFacade() {
        serviceEndpointScheduler.perform();

        verify(serviceEndpointFacade).verifyServiceEndpoints();
    }

    @Test
    @DisplayName("Perform logs info message when scheduled")
    void performLogsInfoMessageWhenScheduled() {
        Logger logger = mock(Logger.class);
        when(logger.isInfoEnabled()).thenReturn(true);

        serviceEndpointScheduler.perform();
    }
}
