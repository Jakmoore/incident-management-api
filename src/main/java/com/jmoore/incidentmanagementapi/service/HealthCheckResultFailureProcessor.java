package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.api.HealthCheckResult;
import com.jmoore.incidentmanagementapi.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckResultFailureProcessor {

    private final NotificationService notificationService;
    private final IncidentService incidentService;

    public void process(HealthCheckResult healthCheckResult) {
        raiseNotification(healthCheckResult);
        createIncident(healthCheckResult);
    }

    private void raiseNotification(HealthCheckResult result) {
        log.warn(
                "Healthcheck for monitor {} failed. Raising notification for callback email: {}",
                result.url(),
                result.callbackEmail()
        );

        notificationService.raiseNotification(
                new Notification(
                        result.failureType(),
                        result.url(),
                        result.expectedStatus(),
                        result.actualStatus(),
                        result.callbackEmail()
                )
        );
    }

    private void createIncident(HealthCheckResult result) {
        log.info("Creating incident log for monitor ID: {}", result.monitorId());
        incidentService.createIncident(result.monitorId(), result.failureType());
    }
}
