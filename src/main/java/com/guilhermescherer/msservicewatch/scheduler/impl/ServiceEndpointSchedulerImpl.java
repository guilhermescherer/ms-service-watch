package com.guilhermescherer.msservicewatch.scheduler.impl;

import com.guilhermescherer.msservicewatch.facade.ServiceEndpointFacade;
import com.guilhermescherer.msservicewatch.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ServiceEndpointSchedulerImpl implements Scheduler {

    private final ServiceEndpointFacade serviceEndpointFacade;

    @Scheduled(fixedDelayString = "${scheduler.delay}")
    @Override
    public void perform() {
        log.info("Performing scheduled service endpoints");

        serviceEndpointFacade.verifyServiceEndpoints();
    }
}
