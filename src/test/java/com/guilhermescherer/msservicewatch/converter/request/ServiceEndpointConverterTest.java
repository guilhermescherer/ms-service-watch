package com.guilhermescherer.msservicewatch.converter.request;

import com.guilhermescherer.msservicewatch.dto.request.ServiceEndpointRequest;
import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceEndpointConverterTest {

    @InjectMocks
    private ServiceEndpointConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should convert ServiceEndpointRequest to ServiceEndpoint with correct fields")
    void shouldConvertRequestToEntity() {
        ServiceEndpointRequest request = new ServiceEndpointRequest();
        request.setName("My Service");
        request.setUrl("https://myservice.co");
        request.setActive(true);
        request.setCheckInterval(15);

        ServiceEndpoint result = converter.convert(request);

        assertNotNull(result);
        assertEquals("My Service", result.getName());
        assertEquals("https://myservice.com", result.getUrl());
        assertTrue(result.isActive());
        assertEquals(15, result.getCheckInterval());
    }
}
