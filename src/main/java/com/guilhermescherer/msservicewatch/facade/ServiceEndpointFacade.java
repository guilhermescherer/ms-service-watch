package com.guilhermescherer.msservicewatch.facade;

import com.guilhermescherer.msservicewatch.dto.request.ServiceEndpointRequest;
import com.guilhermescherer.msservicewatch.dto.response.ServiceEndpointResponse;

import java.util.List;

public interface ServiceEndpointFacade {

    ServiceEndpointResponse create(ServiceEndpointRequest request);

    ServiceEndpointResponse getById(Long id);

    List<ServiceEndpointResponse> getAll();

    ServiceEndpointResponse update(Long id, ServiceEndpointRequest request);

    void delete(Long id);

    void verifyServiceEndpoints();
}
