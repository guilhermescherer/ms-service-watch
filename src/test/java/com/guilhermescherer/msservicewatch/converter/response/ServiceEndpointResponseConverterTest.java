package com.guilhermescherer.msservicewatch.converter.response;

import com.guilhermescherer.msservicewatch.dto.response.ServiceEndpointResponse;
import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import com.guilhermescherer.msservicewatch.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceEndpointResponseConverterTest {

    @InjectMocks
    private ServiceEndpointResponseConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should convert ServiceEndpoint to ServiceEndpointResponse correctly")
    void shouldConvertEntityToResponse() {
        ServiceEndpoint source = new ServiceEndpoint();
        source.setName("My Service");
        source.setUrl("https://myservice.com");
        source.setActive(true);
        source.setLastStatus(Status.UP);
        source.setCheckInterval(180);

        ServiceEndpointResponse response = converter.convert(source);

        assertNotNull(response);
        assertEquals(180, response.getCheckInterval());
        assertEquals("My Service", response.getName());
        assertEquals("https://myservice.com", response.getUrl());
        assertTrue(response.isActive());
        assertEquals("UP", response.getLastStatus());
    }
}
