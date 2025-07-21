package com.guilhermescherer.msservicewatch.repository;

import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLogModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceStatusLogRepository extends JpaRepository<ServiceStatusLogModel, Long> {

    Page<ServiceStatusLogModel> findAllByService(ServiceEndpointModel serviceEndpoint, Pageable pageable);
}
