package com.jmoore.incidentmanagementapi.model.dto.monitor;

import com.jmoore.incidentmanagementapi.model.entity.monitor.MaintenanceWindow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonitorResponseDto {

    private Long id;
    private String name;
    private String url;
    private int expectedStatus;
    private int intervalSeconds;
    private boolean active;
    private String callbackEmail;
    private LocalDateTime createdAt;
    private List<String> tags;
    private MaintenanceWindow maintenanceWindow;
    private long expectedLatency;
}
