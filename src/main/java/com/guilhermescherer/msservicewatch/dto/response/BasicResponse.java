package com.guilhermescherer.msservicewatch.dto.response;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@SuperBuilder(setterPrefix = "with")
@Getter
@Setter
public abstract class BasicResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
