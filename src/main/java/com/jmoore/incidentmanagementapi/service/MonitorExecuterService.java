package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonitorExecuterService {

    private final HealthCheckExecutor healthCheckExecutor;

    @Async
    public void runMonitor(Monitor monitor) {
        healthCheckExecutor.executeHealthCheck(monitor.getUrl(), monitor.getExpectedStatus(), monitor.getCallbackUrl());
    }
}
