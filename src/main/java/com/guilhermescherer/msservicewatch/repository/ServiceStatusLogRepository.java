package com.guilhermescherer.msservicewatch.repository;

import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceStatusLogRepository extends JpaRepository<ServiceStatusLog, Long> {

    Page<ServiceStatusLog> findAllByService(ServiceEndpoint serviceEndpoint, Pageable pageable);
}
