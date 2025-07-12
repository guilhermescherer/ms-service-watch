package com.guilhermescherer.msservicewatch.service.database;

import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceStatusLogDatabaseService {

    void save(ServiceStatusLog serviceStatusLog);

    Page<ServiceStatusLog> getLogsByServiceEndpoint(ServiceEndpoint serviceEndpoint, Pageable pageable);
}
