package com.guilhermescherer.msservicewatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "service_status_log")
@Entity
public class ServiceStatusLog extends Model {

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
    @Column(name = "response_time_millis")
    private Long responseTimeMillis;
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEndpoint service;
}
