package com.guilhermescherer.msservicewatch.service.database;

import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLogModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceStatusLogDatabaseService {

    void save(ServiceStatusLogModel serviceStatusLog);

    Page<ServiceStatusLogModel> getLogsByServiceEndpoint(ServiceEndpointModel serviceEndpoint, Pageable pageable);
}
