package com.jmoore.incidentmanagementapi.model.entity.metrics;

import java.math.BigDecimal;

public record AverageMonitorMetrics(
        long totalMonitors,
        long totalChecks,
        long successfulChecks,
        long failedChecks,
        BigDecimal uptimePercentage,
        long incidents) {
}
