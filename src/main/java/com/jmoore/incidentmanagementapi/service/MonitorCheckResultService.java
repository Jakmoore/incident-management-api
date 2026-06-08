package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.api.HealthCheckResult;
import com.jmoore.incidentmanagementapi.model.dto.MetricsResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import com.jmoore.incidentmanagementapi.model.entity.MonitorCheckResult;
import com.jmoore.incidentmanagementapi.model.entity.MonitorMetricsPartial;
import com.jmoore.incidentmanagementapi.repository.MonitorCheckResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorCheckResultService {

    private final MonitorCheckResultRepository resultRepository;
    private final IncidentService incidentService;

    @Transactional
    public void processMonitorCheckResult(Monitor monitor, HealthCheckResult healthCheckResult) {
        MonitorCheckResult monitorCheckResult = MonitorCheckResult.builder()
                .monitor(monitor)
                .statusCode(healthCheckResult.actualStatus())
                .latencyMs(healthCheckResult.latency())
                .success(healthCheckResult.success())
                .failureReason(healthCheckResult.failureType().name())
                .build();

        resultRepository.save(monitorCheckResult);
    }

    public MetricsResponseDto getMetrics(long monitorId, LocalDateTime cutoffDateTime) {
        MonitorMetricsPartial partial = resultRepository.getMetrics(monitorId, cutoffDateTime);

        return MetricsResponseDto.builder()
                .monitorId(monitorId)
                .totalChecks(partial.getTotalChecks())
                .successfulChecks(partial.getSuccessfulChecks())
                .failedChecks(partial.getTotalChecks() - partial.getSuccessfulChecks())
                .uptimePercentage((double) partial.getSuccessfulChecks() / partial.getTotalChecks())
                .averageLatencyMs(partial.getAverageLatencyMs())
                .openIncidents(incidentService.getOpenIncidentCount(monitorId))
                .build();
    }
}
