package com.guilhermescherer.msservicewatch.converter.request;

import com.guilhermescherer.msservicewatch.dto.request.ServiceEndpointRequest;
import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;
import org.springframework.stereotype.Component;

@Component
public class ServiceEndpointUpdateConverter implements UpdateConverter<ServiceEndpointRequest, ServiceEndpointModel> {

    @Override
    public ServiceEndpointModel convert(ServiceEndpointRequest source, ServiceEndpointModel target) {
        target.setName(source.getName());
        target.setUrl(source.getUrl());
        target.setActive(source.getActive());
        target.setCheckInterval(source.getCheckInterval());

        return target;
    }
}
