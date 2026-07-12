package com.jmoore.incidentmanagementapi.model.entity.metrics;

public interface MonitorMetricsPartial {

    long getTotalChecks();
    long getSuccessfulChecks();
    Double getAverageLatencyMs();
}
