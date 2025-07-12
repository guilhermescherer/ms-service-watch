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
public class ServiceStatusLogResponse extends BasicResponse {

    private LocalDateTime checkedAt;
    private String status;
    private Long responseTimeMillis;
}
