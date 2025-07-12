package com.guilhermescherer.msservicewatch.facade.impl;

import com.guilhermescherer.msservicewatch.converter.response.ServiceLogStatusResponseConverter;
import com.guilhermescherer.msservicewatch.dto.response.ServiceStatusLogResponse;
import com.guilhermescherer.msservicewatch.exception.NotFoundException;
import com.guilhermescherer.msservicewatch.facade.ServiceStatusLogFacade;
import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import com.guilhermescherer.msservicewatch.service.database.ServiceEndpointDatabaseService;
import com.guilhermescherer.msservicewatch.service.database.ServiceStatusLogDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceStatusLogFacadeImpl implements ServiceStatusLogFacade {

    private final ServiceEndpointDatabaseService serviceEndpointDatabaseService;
    private final ServiceStatusLogDatabaseService serviceStatusLogDatabaseService;
    private final ServiceLogStatusResponseConverter serviceLogStatusResponseConverter;

    @Override
    public Page<ServiceStatusLogResponse> getLogsByServiceEndpointId(Long id, Pageable pageable) {
        ServiceEndpoint serviceEndpoint = serviceEndpointDatabaseService.getById(id)
                .orElseThrow(NotFoundException::new);

        return serviceStatusLogDatabaseService.getLogsByServiceEndpoint(serviceEndpoint, pageable)
                .map(serviceLogStatusResponseConverter::convert);
    }
}
