package com.guilhermescherer.msservicewatch.service.api.impl;

import com.guilhermescherer.msservicewatch.data.ResponseData;
import com.guilhermescherer.msservicewatch.model.ServiceEndpoint;
import com.guilhermescherer.msservicewatch.service.api.ServiceEndpointApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ServiceEndpointApiServiceImpl implements ServiceEndpointApiService {

    private final WebClient webClient;

    @Override
    public ResponseData callEndpoint(ServiceEndpoint serviceEndpoint) {
        Instant start = Instant.now();

        try {
            ResponseEntity<Void> responseEntity = Optional.ofNullable(webClient.get()
                            .uri(serviceEndpoint.getUrl())
                            .retrieve()
                            .toBodilessEntity()
                            .block())
                    .orElseThrow(() -> new RuntimeException("Null response entity"));

            return ResponseData.builder()
                    .withHttpStatusCode(responseEntity.getStatusCode())
                    .withResponseTimeMillis(getTotalTimeMillis(start))
                    .build();
        } catch (WebClientResponseException ex) {
            return ResponseData.builder()
                    .withHttpStatusCode(ex.getStatusCode())
                    .withResponseTimeMillis(getTotalTimeMillis(start))
                    .build();
        } catch (Exception ex) {
            log.error("Some error occurred while trying to call endpoint {}", serviceEndpoint.getUrl(), ex);

            return ResponseData.builder().build();
        }
    }

    private long getTotalTimeMillis(Instant start) {
        Duration duration = Duration.between(start, Instant.now());
        return duration.toMillis();
    }
}
