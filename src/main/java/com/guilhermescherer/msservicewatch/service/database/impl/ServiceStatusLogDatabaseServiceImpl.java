package com.guilhermescherer.msservicewatch.service.database.impl;

import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLogModel;
import com.guilhermescherer.msservicewatch.repository.ServiceStatusLogRepository;
import com.guilhermescherer.msservicewatch.service.database.ServiceStatusLogDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ServiceStatusLogDatabaseServiceImpl implements ServiceStatusLogDatabaseService {

    private final ServiceStatusLogRepository serviceStatusLogRepository;

    @Override
    public void save(ServiceStatusLogModel serviceStatusLog) {
        serviceStatusLogRepository.save(serviceStatusLog);
    }

    @Override
    public Page<ServiceStatusLogModel> getLogsByServiceEndpoint(ServiceEndpointModel serviceEndpoint, Pageable pageable) {
        return serviceStatusLogRepository.findAllByService(serviceEndpoint, pageable);
    }
}
