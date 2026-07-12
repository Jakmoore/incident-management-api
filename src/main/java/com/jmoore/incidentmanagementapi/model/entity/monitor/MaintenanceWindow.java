package com.jmoore.incidentmanagementapi.model.entity.monitor;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Embeddable
@AllArgsConstructor
public class MaintenanceWindow {

    private LocalDateTime start;
    private LocalDateTime end;

    protected MaintenanceWindow() {

    }
}
