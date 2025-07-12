package com.guilhermescherer.msservicewatch.service.database.impl;

import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import com.guilhermescherer.msservicewatch.repository.ServiceEndpointRepository;
import com.guilhermescherer.msservicewatch.service.database.ServiceEndpointDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ServiceEndpointDatabaseServiceImpl implements ServiceEndpointDatabaseService {

    private final ServiceEndpointRepository serviceEndpointRepository;

    @Override
    public ServiceEndpoint save(ServiceEndpoint serviceEndpoint) {
        return serviceEndpointRepository.save(serviceEndpoint);
    }

    @Override
    public Optional<ServiceEndpoint> getById(Long id) {
        return serviceEndpointRepository.findById(id);
    }

    @Override
    public List<ServiceEndpoint> getAll() {
        return serviceEndpointRepository.findAll();
    }

    @Override
    public List<ServiceEndpoint> getNextEndpointsToCheck() {
        return serviceEndpointRepository.findServiceEndpointsToVerify();
    }

    @Override
    public void delete(Long id) {
        serviceEndpointRepository.deleteById(id);
    }
}
