package com.guilhermescherer.msservicewatch.service.database;

import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;

import java.util.List;
import java.util.Optional;

public interface ServiceEndpointDatabaseService {

    ServiceEndpointModel save(ServiceEndpointModel serviceEndpoint);

    Optional<ServiceEndpointModel> getById(Long id);

    List<ServiceEndpointModel> getAll();

    List<ServiceEndpointModel> getNextEndpointsToCheck();

    void delete(Long id);
}
