package com.guilhermescherer.msservicewatch.converter.request;

import com.guilhermescherer.msservicewatch.dto.request.ServiceEndpointRequest;
import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

class ServiceEndpointUpdateConverterTest {

    @InjectMocks
    private ServiceEndpointUpdateConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should update target ServiceEndpoint with fields from source ServiceEndpointRequest")
    void shouldUpdateTargetEntityWithSourceFields() {
        ServiceEndpointRequest source = new ServiceEndpointRequest();
        source.setName("Updated Service");
        source.setUrl("https://updatedservice.com");
        source.setActive(false);
        source.setCheckInterval(30);

        ServiceEndpointModel target = new ServiceEndpointModel();
        target.setName("Old Service");
        target.setUrl("https://oldservice.com");
        target.setActive(true);
        target.setCheckInterval(10);

        ServiceEndpointModel result = converter.convert(source, target);

        assertSame(target, result, "Returned object should be the same target instance");
        assertEquals("Updated Service", target.getName());
        assertEquals("https://updatedservice.com", target.getUrl());
        assertFalse(target.isActive());
        assertEquals(30, target.getCheckInterval());
    }
}
