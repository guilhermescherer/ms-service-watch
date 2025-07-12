package com.guilhermescherer.msservicewatch.controller;

import com.guilhermescherer.msservicewatch.dto.response.ServiceStatusLogResponse;
import com.guilhermescherer.msservicewatch.facade.ServiceStatusLogFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service-endpoint/{id}/logs")
public class ServiceStatusLogController {

    private final ServiceStatusLogFacade serviceStatusLogFacade;

    @GetMapping
    public ResponseEntity<Page<ServiceStatusLogResponse>> getLogsByServiceEndpointId(@PathVariable Long id,
                                                                                     @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ServiceStatusLogResponse> response = serviceStatusLogFacade.getLogsByServiceEndpointId(id, pageable);
        return ResponseEntity.ok().body(response);
    }
}
