package com.jmoore.incidentmanagementapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "monitor_id", nullable = false)
    private Monitor monitor;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "incident_type", nullable = false, length = 100)
    private String incidentType;

    @Column(name = "expected_status")
    private Integer expectedStatus;

    @Column(name = "actual_status")
    private Integer actualStatus;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "callback_email", length = 500)
    private String callbackEmail;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "fingerprint", updatable = false)
    private String fingerprint;

    @Column(name = "open_incident")
    private Boolean openIncident;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}