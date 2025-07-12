package com.guilhermescherer.msservicewatch.facade;

import com.guilhermescherer.msservicewatch.dto.response.ServiceStatusLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceStatusLogFacade {

    Page<ServiceStatusLogResponse> getLogsByServiceEndpointId(Long id, Pageable pageable);
}
