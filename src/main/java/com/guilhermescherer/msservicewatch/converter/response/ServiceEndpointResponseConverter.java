package com.guilhermescherer.msservicewatch.converter.response;

import com.guilhermescherer.msservicewatch.dto.response.ServiceEndpointResponse;
import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServiceEndpointResponseConverter implements Converter<ServiceEndpoint, ServiceEndpointResponse> {

    @Override
    public ServiceEndpointResponse convert(ServiceEndpoint source) {
        return ServiceEndpointResponse.builder()
                .withUrl(source.getUrl())
                .withName(source.getName())
                .withId(source.getId())
                .withActive(source.isActive())
                .withCreatedAt(source.getCreatedAt())
                .withUpdatedAt(source.getUpdatedAt())
                .withLastCheckTime(source.getCheckedAt())
                .withLastStatus(Optional.ofNullable(source.getLastStatus()).map(Enum::toString).orElse(null))
                .build();
    }
}
