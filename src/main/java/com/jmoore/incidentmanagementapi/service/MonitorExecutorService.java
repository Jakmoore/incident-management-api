package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.api.HealthCheckResult;
import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonitorExecutorService {

    private final MonitorService monitorService;
    private final HealthCheckExecutor healthCheckExecutor;
    private final HealthCheckResultProcessor resultProcessor;

    public void runMonitor(long monitorId) {
        Monitor monitor = monitorService.getEntityById(monitorId);
        runMonitor(monitor);
    }

    @Async
    public void runMonitor(Monitor monitor) {
        HealthCheckResult result = healthCheckExecutor.executeHealthCheck(monitor);
        resultProcessor.process(result);
        monitorService.updateNextRunAt(monitor);
    }
}
