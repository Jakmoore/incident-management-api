package com.jmoore.incidentmanagementapi.model.dto.metrics;

import java.math.BigDecimal;

public record AverageMonitorMetricsResponseDto(
        long totalMonitors,
        long totalChecks,
        long successfulChecks,
        long failedChecks,
        BigDecimal uptimePercentage,
        long incidents) {
}
