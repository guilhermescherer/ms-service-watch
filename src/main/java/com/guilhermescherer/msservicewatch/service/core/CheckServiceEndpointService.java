package com.guilhermescherer.msservicewatch.service.core;

import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;

public interface CheckServiceEndpointService {

    void check(ServiceEndpointModel serviceEndpoint);
}
