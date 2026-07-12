package com.jmoore.incidentmanagementapi.model.dto.metrics;

import lombok.Builder;

@Builder
public record MetricsResponseDto(
        Long monitorId,
        String window,
        long totalChecks,
        long successfulChecks,
        long failedChecks,
        double uptimePercentage,
        Double averageLatencyMs,
        Long openIncidents) {
}
