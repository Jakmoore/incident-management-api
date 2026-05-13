package com.jmoore.incidentmanagementapi.model.api;

import com.jmoore.incidentmanagementapi.model.notification.FailureType;

public record HealthCheckResult(
        boolean success,
        FailureType failureType,
        Integer actualStatus,
        Integer expectedStatus,
        Long monitorId,
        String url,
        String callbackEmail) {
}
