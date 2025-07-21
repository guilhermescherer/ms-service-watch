package com.guilhermescherer.msservicewatch.facade.impl;

import com.guilhermescherer.msservicewatch.converter.request.ServiceEndpointConverter;
import com.guilhermescherer.msservicewatch.converter.request.ServiceEndpointUpdateConverter;
import com.guilhermescherer.msservicewatch.converter.response.ServiceEndpointResponseConverter;
import com.guilhermescherer.msservicewatch.dto.request.ServiceEndpointRequest;
import com.guilhermescherer.msservicewatch.dto.response.ServiceEndpointResponse;
import com.guilhermescherer.msservicewatch.exception.NotFoundException;
import com.guilhermescherer.msservicewatch.facade.ServiceEndpointFacade;
import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;
import com.guilhermescherer.msservicewatch.service.core.CheckServiceEndpointService;
import com.guilhermescherer.msservicewatch.service.database.ServiceEndpointDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class ServiceEndpointFacadeImpl implements ServiceEndpointFacade {

    private final ServiceEndpointDatabaseService serviceEndpointDatabaseService;
    private final CheckServiceEndpointService checkServiceEndpointService;
    private final ServiceEndpointConverter serviceEndpointConverter;
    private final ServiceEndpointResponseConverter serviceEndpointResponseConverter;
    private final ServiceEndpointUpdateConverter serviceEndpointUpdateConverter;

    @Override
    public ServiceEndpointResponse create(ServiceEndpointRequest request) {
        ServiceEndpointModel serviceEndpoint = serviceEndpointConverter.convert(request);
        serviceEndpoint = serviceEndpointDatabaseService.save(serviceEndpoint);
        return serviceEndpointResponseConverter.convert(serviceEndpoint);
    }

    @Override
    public ServiceEndpointResponse getById(Long id) {
        ServiceEndpointModel serviceEndpoint = serviceEndpointDatabaseService.getById(id)
                .orElseThrow(NotFoundException::new);

        return serviceEndpointResponseConverter.convert(serviceEndpoint);
    }

    @Override
    public List<ServiceEndpointResponse> getAll() {
        List<ServiceEndpointModel> services = serviceEndpointDatabaseService.getAll();

        return Stream.ofNullable(services)
                .flatMap(Collection::stream)
                .map(serviceEndpointResponseConverter::convert)
                .toList();
    }

    @Override
    public ServiceEndpointResponse update(Long id, ServiceEndpointRequest request) {
        ServiceEndpointModel serviceEndpoint = serviceEndpointDatabaseService.getById(id)
                .orElseThrow(NotFoundException::new);

        serviceEndpoint = serviceEndpointUpdateConverter.convert(request, serviceEndpoint);
        serviceEndpoint = serviceEndpointDatabaseService.save(serviceEndpoint);

        return serviceEndpointResponseConverter.convert(serviceEndpoint);
    }

    @Override
    public void delete(Long id) {
        serviceEndpointDatabaseService.delete(id);
    }

    @Override
    public void verifyServiceEndpoints() {
        Stream.ofNullable(serviceEndpointDatabaseService.getNextEndpointsToCheck())
                .flatMap(Collection::stream)
                .forEach(checkServiceEndpointService::check);
    }
}
