package com.guilhermescherer.msservicewatch.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(setterPrefix = "with")
@Getter
@Setter
public class ServiceEndpointResponse extends BasicResponse {

    private String name;
    private String url;
    private boolean active;
    private String lastStatus;
    private LocalDateTime lastCheckTime;
}
