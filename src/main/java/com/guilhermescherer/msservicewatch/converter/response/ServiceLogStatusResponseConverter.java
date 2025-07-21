package com.guilhermescherer.msservicewatch.converter.response;

import com.guilhermescherer.msservicewatch.dto.response.ServiceStatusLogResponse;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLogModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServiceLogStatusResponseConverter implements Converter<ServiceStatusLogModel, ServiceStatusLogResponse> {

    @Override
    public ServiceStatusLogResponse convert(ServiceStatusLogModel source) {
        return ServiceStatusLogResponse.builder()
                .withStatus(source.getStatus().toString())
                .withResponseTimeMillis(source.getResponseTimeMillis())
                .withId(source.getId())
                .withCreatedAt(source.getCreatedAt())
                .withUpdatedAt(source.getUpdatedAt())
                .withCheckedAt(source.getCheckedAt())
                .build();
    }
}
