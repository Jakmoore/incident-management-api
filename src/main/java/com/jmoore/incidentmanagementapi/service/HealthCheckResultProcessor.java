package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.api.HealthCheckResult;
import com.jmoore.incidentmanagementapi.model.entity.Incident;
import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import com.jmoore.incidentmanagementapi.model.notification.FailureNotification;
import com.jmoore.incidentmanagementapi.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckResultProcessor {

    private final MonitorService monitorService;
    private final MonitorCheckResultService monitorCheckResultService;
    private final IncidentService incidentService;
    private final IncidentFingerprintGenerator fingerprintGenerator;
    private final NotificationService notificationService;

    @Transactional
    public void process(HealthCheckResult healthCheckResult) {
        Monitor monitor = monitorService.getEntityById(healthCheckResult.monitorId());

        monitorCheckResultService.processMonitorCheckResult(monitor, healthCheckResult);

        if (healthCheckResult.success()) {
            monitor.recordSuccess();

            if (monitor.getConsecutiveSuccesses() >= 3) {
                String fingerprint = fingerprintGenerator.generate(monitor.getId() + monitor.getUrl() + healthCheckResult.failureType().name() + monitor.getCallbackEmail());
                Optional<Incident> incident = incidentService.getLastOpenIncidentByFingerprint(fingerprint);
                incident.ifPresent(incidentService::resolveIncident);
                raiseResolutionNotification(healthCheckResult);
            }

            monitorService.save(monitor);
            return;
        }

        monitor.recordFailure();

        if (monitor.getConsecutiveFailures() >= 3) {
            boolean incidentRaised = incidentService.processIncident(healthCheckResult.monitorId(), healthCheckResult.failureType(), healthCheckResult.actualStatus());

            // Only send notification if an incident was raised. We don't want to send many notifications for the same issue
            if (incidentRaised) {
                log.warn(
                        "Healthcheck for monitor {} failed. Raising notification for callback email: {}",
                        healthCheckResult.url(),
                        healthCheckResult.callbackEmail()
                );

                raiseFailureNotification(healthCheckResult);
            }
        }
    }

    private void raiseResolutionNotification(HealthCheckResult result) {
        notificationService.raiseSuccessNotification(
                new Notification(
                        result.url(),
                        result.expectedStatus(),
                        result.actualStatus(),
                        result.callbackEmail()
                )
        );
    }

    private void raiseFailureNotification(HealthCheckResult result) {
        notificationService.raiseFailureNotifications(
                new FailureNotification(
                        result.url(),
                        result.expectedStatus(),
                        result.actualStatus(),
                        result.callbackEmail(),
                        result.failureType()
                )
        );
    }
}
