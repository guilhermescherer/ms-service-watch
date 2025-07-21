package com.guilhermescherer.msservicewatch.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "service_endpoint")
@Entity
public class ServiceEndpointModel extends Model {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "url", nullable = false)
    private String url;
    @Column(name = "active", nullable = false)
    private boolean active;
    @Column(name = "check_interval", nullable = false)
    private int checkInterval;
    @Column(name = "checked_at")
    private LocalDateTime checkedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "last_status")
    private Status lastStatus;
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceStatusLogModel> logs;
}
