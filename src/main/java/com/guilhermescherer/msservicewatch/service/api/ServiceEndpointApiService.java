package com.guilhermescherer.msservicewatch.service.api;

import com.guilhermescherer.msservicewatch.data.ResponseData;
import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;

public interface ServiceEndpointApiService {
    ResponseData callEndpoint(ServiceEndpoint serviceEndpoint);
}
