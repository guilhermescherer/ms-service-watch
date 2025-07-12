package com.guilhermescherer.msservicewatch.service.database;

import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;

import java.util.List;
import java.util.Optional;

public interface ServiceEndpointDatabaseService {

    ServiceEndpoint save(ServiceEndpoint serviceEndpoint);

    Optional<ServiceEndpoint> getById(Long id);

    List<ServiceEndpoint> getAll();

    List<ServiceEndpoint> getNextEndpointsToCheck();

    void delete(Long id);
}
