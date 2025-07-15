package com.guilhermescherer.msservicewatch.converter.response;

import com.guilhermescherer.msservicewatch.dto.response.ServiceStatusLogResponse;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLog;
import com.guilhermescherer.msservicewatch.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceLogStatusResponseConverterTest {

    @InjectMocks
    private ServiceLogStatusResponseConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should convert ServiceStatusLog to ServiceStatusLogResponse correctly")
    void shouldConvertEntityToResponse() {
        ServiceStatusLog source = new ServiceStatusLog();
        source.setStatus(Status.UP);
        source.setResponseTimeMillis(250L);
        source.setCheckedAt(LocalDateTime.now());

        ServiceStatusLogResponse response = converter.convert(source);

        assertNotNull(response);
        assertEquals("UP", response.getStatus());
        assertEquals(250L, response.getResponseTimeMillis());
        assertEquals(source.getCheckedAt(), response.getCheckedAt());
    }
}
