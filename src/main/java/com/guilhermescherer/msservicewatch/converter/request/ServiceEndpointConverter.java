package com.guilhermescherer.msservicewatch.converter.request;

import com.guilhermescherer.msservicewatch.dto.request.ServiceEndpointRequest;
import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServiceEndpointConverter implements Converter<ServiceEndpointRequest, ServiceEndpoint> {

    @Override
    public ServiceEndpoint convert(ServiceEndpointRequest source) {
        ServiceEndpoint target = new ServiceEndpoint();

        target.setName(source.getName());
        target.setUrl(source.getUrl());
        target.setActive(source.getActive());
        target.setCheckInterval(source.getCheckInterval());

        return target;
    }
}
