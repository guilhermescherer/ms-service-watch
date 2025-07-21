package com.guilhermescherer.msservicewatch.facade.impl;

import com.guilhermescherer.msservicewatch.converter.request.ServiceEndpointConverter;
import com.guilhermescherer.msservicewatch.converter.request.ServiceEndpointUpdateConverter;
import com.guilhermescherer.msservicewatch.converter.response.ServiceEndpointResponseConverter;
import com.guilhermescherer.msservicewatch.dto.request.ServiceEndpointRequest;
import com.guilhermescherer.msservicewatch.dto.response.ServiceEndpointResponse;
import com.guilhermescherer.msservicewatch.exception.NotFoundException;
import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;
import com.guilhermescherer.msservicewatch.service.core.CheckServiceEndpointService;
import com.guilhermescherer.msservicewatch.service.database.ServiceEndpointDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ServiceEndpointFacadeImplTest {

    @Mock
    private ServiceEndpointDatabaseService dbService;
    @Mock
    private CheckServiceEndpointService checkService;
    @Mock
    private ServiceEndpointConverter converter;
    @Mock
    private ServiceEndpointResponseConverter responseConverter;
    @Mock
    private ServiceEndpointUpdateConverter updateConverter;
    @InjectMocks
    private ServiceEndpointFacadeImpl facade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create and return response when saving new service endpoint")
    void shouldCreateAndReturnResponse() {
        ServiceEndpointRequest request = mock(ServiceEndpointRequest.class);
        ServiceEndpointModel endpoint = mock(ServiceEndpointModel.class);
        ServiceEndpointModel savedEndpoint = mock(ServiceEndpointModel.class);
        ServiceEndpointResponse response = mock(ServiceEndpointResponse.class);

        when(converter.convert(request)).thenReturn(endpoint);
        when(dbService.save(endpoint)).thenReturn(savedEndpoint);
        when(responseConverter.convert(savedEndpoint)).thenReturn(response);

        ServiceEndpointResponse result = facade.create(request);

        assertEquals(response, result);
        verify(converter).convert(request);
        verify(dbService).save(endpoint);
        verify(responseConverter).convert(savedEndpoint);
    }

    @Test
    @DisplayName("Should return converted response when endpoint exists")
    void shouldReturnConvertedResponseWhenEndpointExists() {
        ServiceEndpointModel endpoint = mock(ServiceEndpointModel.class);
        ServiceEndpointResponse response = mock(ServiceEndpointResponse.class);

        when(dbService.getById(1L)).thenReturn(Optional.of(endpoint));
        when(responseConverter.convert(endpoint)).thenReturn(response);

        ServiceEndpointResponse result = facade.getById(1L);

        assertEquals(response, result);
        verify(dbService).getById(1L);
        verify(responseConverter).convert(endpoint);
    }

    @Test
    @DisplayName("Should throw NotFoundException when endpoint does not exist")
    void shouldThrowNotFoundWhenEndpointNotFound() {
        when(dbService.getById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> facade.getById(2L));
        verify(dbService).getById(2L);
    }

    @Test
    @DisplayName("Should return list of converted responses")
    void shouldReturnListOfConvertedResponses() {
        ServiceEndpointModel endpoint1 = mock(ServiceEndpointModel.class);
        ServiceEndpointModel endpoint2 = mock(ServiceEndpointModel.class);
        ServiceEndpointResponse response1 = mock(ServiceEndpointResponse.class);
        ServiceEndpointResponse response2 = mock(ServiceEndpointResponse.class);

        when(dbService.getAll()).thenReturn(Arrays.asList(endpoint1, endpoint2));
        when(responseConverter.convert(endpoint1)).thenReturn(response1);
        when(responseConverter.convert(endpoint2)).thenReturn(response2);

        List<ServiceEndpointResponse> result = facade.getAll();

        assertEquals(Arrays.asList(response1, response2), result);
        verify(responseConverter).convert(endpoint1);
        verify(responseConverter).convert(endpoint2);
    }

    @Test
    @DisplayName("Should return empty list when no endpoints exist")
    void shouldReturnEmptyListWhenNoEndpointsExist() {
        when(dbService.getAll()).thenReturn(Collections.emptyList());

        List<ServiceEndpointResponse> result = facade.getAll();

        assertTrue(result.isEmpty());
        verify(dbService).getAll();
    }

    @Test
    @DisplayName("Should update and return converted response")
    void shouldUpdateAndReturnResponse() {
        ServiceEndpointRequest request = mock(ServiceEndpointRequest.class);
        ServiceEndpointModel existing = mock(ServiceEndpointModel.class);
        ServiceEndpointModel updated = mock(ServiceEndpointModel.class);
        ServiceEndpointModel saved = mock(ServiceEndpointModel.class);
        ServiceEndpointResponse response = mock(ServiceEndpointResponse.class);

        when(dbService.getById(3L)).thenReturn(Optional.of(existing));
        when(updateConverter.convert(request, existing)).thenReturn(updated);
        when(dbService.save(updated)).thenReturn(saved);
        when(responseConverter.convert(saved)).thenReturn(response);

        ServiceEndpointResponse result = facade.update(3L, request);

        assertEquals(response, result);
    }

    @Test
    @DisplayName("Should throw NotFoundException when trying to update nonexistent endpoint")
    void shouldThrowNotFoundWhenUpdatingNonexistentEndpoint() {
        ServiceEndpointRequest request = mock(ServiceEndpointRequest.class);
        when(dbService.getById(4L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> facade.update(4L, request));
    }

    @Test
    @DisplayName("Should call database delete with correct ID")
    void shouldCallDeleteWithCorrectId() {
        facade.delete(5L);
        verify(dbService).delete(5L);
    }

    @Test
    @DisplayName("Should call check on each endpoint returned by DB")
    void shouldCallCheckForEachEndpoint() {
        ServiceEndpointModel endpoint1 = mock(ServiceEndpointModel.class);
        ServiceEndpointModel endpoint2 = mock(ServiceEndpointModel.class);

        when(dbService.getNextEndpointsToCheck()).thenReturn(Arrays.asList(endpoint1, endpoint2));

        facade.verifyServiceEndpoints();

        verify(checkService).check(endpoint1);
        verify(checkService).check(endpoint2);
    }

    @Test
    @DisplayName("Should do nothing if no endpoints to check")
    void shouldDoNothingWhenNoEndpoints() {
        when(dbService.getNextEndpointsToCheck()).thenReturn(null);

        facade.verifyServiceEndpoints();

        verifyNoInteractions(checkService);
    }
}
