package com.jmoore.incidentmanagementapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "monitors")
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(name = "expected_status", nullable = false)
    private int expectedStatus = 200;

    @Column(name = "interval_seconds", nullable = false)
    private int intervalSeconds = 60;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "next_run_at", nullable = false, updatable = false)
    private LocalDateTime nextRunAt;

    @Column(name = "callback_email", nullable = false)
    private String callbackEmail;

    @Column(name = "consecutive_failures")
    private Integer consecutiveFailures;

    @Column(name = "consecutive_successes")
    private Integer consecutiveSuccesses;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void recordSuccess() {
        this.consecutiveFailures = 0;
        this.consecutiveSuccesses++;
    }

    public void recordFailure() {
        this.consecutiveSuccesses = 0;
        this.consecutiveFailures++;
    }
}
