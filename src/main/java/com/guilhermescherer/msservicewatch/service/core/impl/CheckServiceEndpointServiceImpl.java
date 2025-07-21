package com.guilhermescherer.msservicewatch.service.core.impl;

import com.guilhermescherer.msservicewatch.data.ResponseData;
import com.guilhermescherer.msservicewatch.model.ServiceEndpointModel;
import com.guilhermescherer.msservicewatch.model.ServiceStatusLogModel;
import com.guilhermescherer.msservicewatch.model.Status;
import com.guilhermescherer.msservicewatch.service.api.ServiceEndpointApiService;
import com.guilhermescherer.msservicewatch.service.core.CheckServiceEndpointService;
import com.guilhermescherer.msservicewatch.service.database.ServiceEndpointDatabaseService;
import com.guilhermescherer.msservicewatch.service.database.ServiceStatusLogDatabaseService;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Service
public class CheckServiceEndpointServiceImpl implements CheckServiceEndpointService {

    private final ServiceEndpointApiService serviceEndpointApiService;
    private final ServiceEndpointDatabaseService serviceEndpointDatabaseService;
    private final ServiceStatusLogDatabaseService serviceStatusLogDatabaseService;

    @Transactional
    @Override
    public void check(ServiceEndpointModel serviceEndpoint) {
        log.info("To check {}", serviceEndpoint.getUrl());

        ResponseData response = serviceEndpointApiService.callEndpoint(serviceEndpoint);

        Status status = StatusByResponseData.from(response);
        LocalDateTime now = LocalDateTime.now();

        updateServiceEndpoint(serviceEndpoint, now, status);
        updateServiceStatusLog(serviceEndpoint, now, status, response);

        log.info("Endpoint {} checked", serviceEndpoint.getUrl());
    }

    private void updateServiceStatusLog(ServiceEndpointModel serviceEndpoint, LocalDateTime now, Status status, ResponseData response) {
        ServiceStatusLogModel serviceStatusLog = new ServiceStatusLogModel();

        serviceStatusLog.setCheckedAt(now);
        serviceStatusLog.setService(serviceEndpoint);
        serviceStatusLog.setStatus(status);
        serviceStatusLog.setResponseTimeMillis(response.getResponseTimeMillis());

        serviceStatusLogDatabaseService.save(serviceStatusLog);
    }

    private void updateServiceEndpoint(ServiceEndpointModel serviceEndpoint, LocalDateTime now, Status status) {
        serviceEndpoint.setCheckedAt(now);
        serviceEndpoint.setLastStatus(status);

        serviceEndpointDatabaseService.save(serviceEndpoint);
    }

    private enum StatusByResponseData {

        UP(Status.UP, response ->
                Optional.ofNullable(response)
                        .map(ResponseData::getHttpStatusCode)
                        .map(HttpStatusCode::is2xxSuccessful)
                        .orElse(false)
        ),

        DOWN(Status.DOWN, response ->
                Optional.ofNullable(response)
                        .map(ResponseData::getHttpStatusCode)
                        .map(code -> !code.is2xxSuccessful())
                        .orElse(false)
        ),

        UNKNOWN(Status.UNKNOWN, response -> false);

        @Getter
        private final Status status;
        private final Predicate<ResponseData> matcher;

        StatusByResponseData(Status status, Predicate<ResponseData> matcher) {
            this.status = status;
            this.matcher = matcher;
        }

        public boolean matches(ResponseData response) {
            return matcher.test(response);
        }

        public static Status from(ResponseData response) {
            for (StatusByResponseData value : values()) {
                if (value.matches(response)) {
                    return value.getStatus();
                }
            }

            return UNKNOWN.getStatus();
        }
    }
}


