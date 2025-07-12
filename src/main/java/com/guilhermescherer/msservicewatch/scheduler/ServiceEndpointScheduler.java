package com.guilhermescherer.msservicewatch.scheduler;

import com.guilhermescherer.msservicewatch.facade.ServiceEndpointFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ServiceEndpointScheduler {

    private final ServiceEndpointFacade serviceEndpointFacade;

    @Scheduled(fixedDelay = 2_000)
    public void perform() {
        log.info("Performing scheduled service endpoints");

        serviceEndpointFacade.verifyServiceEndpoints();
    }
}
