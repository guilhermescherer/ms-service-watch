package com.guilhermescherer.msservicewatch.controller;

import com.guilhermescherer.msservicewatch.dto.request.ServiceEndpointRequest;
import com.guilhermescherer.msservicewatch.dto.response.ServiceEndpointResponse;
import com.guilhermescherer.msservicewatch.facade.ServiceEndpointFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service-endpoint")
@Tag(name = "Service Endpoint")
public class ServiceEndpointController {

    private final ServiceEndpointFacade serviceEndpointFacade;

    @PostMapping
    @Operation(summary = "Create a new service endpoint")
    public ResponseEntity<ServiceEndpointResponse> create(@Valid @RequestBody ServiceEndpointRequest request) {
        ServiceEndpointResponse response = serviceEndpointFacade.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a service endpoint by ID")
    public ResponseEntity<ServiceEndpointResponse> getById(@PathVariable Long id) {
        ServiceEndpointResponse response = serviceEndpointFacade.getById(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    @Operation(summary = "Get all service endpoints")
    public ResponseEntity<List<ServiceEndpointResponse>> getAll() {
        List<ServiceEndpointResponse> response = serviceEndpointFacade.getAll();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a service endpoint by ID")
    public ResponseEntity<ServiceEndpointResponse> update(@PathVariable Long id, @Valid @RequestBody ServiceEndpointRequest request) {
        ServiceEndpointResponse updated = serviceEndpointFacade.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a service endpoint by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceEndpointFacade.delete(id);
        return ResponseEntity.noContent().build();
    }
}
