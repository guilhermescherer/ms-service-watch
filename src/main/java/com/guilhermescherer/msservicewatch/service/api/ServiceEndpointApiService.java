package com.guilhermescherer.msservicewatch.service.api;

import com.guilhermescherer.msservicewatch.data.ResponseData;
import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;

public interface ServiceEndpointApiService {
    ResponseData callEndpoint(ServiceEndpointModel serviceEndpoint);
}
