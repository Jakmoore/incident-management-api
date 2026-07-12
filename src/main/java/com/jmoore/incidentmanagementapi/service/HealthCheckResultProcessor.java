package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.api.HealthCheckResult;
import com.jmoore.incidentmanagementapi.model.entity.incident.Incident;
import com.jmoore.incidentmanagementapi.model.entity.monitor.Monitor;
import com.jmoore.incidentmanagementapi.model.notification.FailureNotification;
import com.jmoore.incidentmanagementapi.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        if (!isMaintenanceWindow(monitor.getMaintenanceWindow().getStart(), monitor.getMaintenanceWindow().getEnd())) {
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

            log.warn("Healthcheck for monitor ID: {} failed with: {}", monitor.getId(), healthCheckResult.failureType());

            monitor.recordFailure();

            if (monitor.getConsecutiveFailures() >= 3) {
                boolean incidentRaised = incidentService.processIncident(healthCheckResult.monitorId(), healthCheckResult.failureType(), healthCheckResult.actualStatus());

                // Only send notification if an incident was raised. We don't want to send many notifications for the same issue
                if (incidentRaised) {
                    log.warn("Raising notification for URL: {} callback email: {}",
                            healthCheckResult.url(),
                            healthCheckResult.callbackEmail()
                    );

                    raiseFailureNotification(healthCheckResult);
                }
            }
        }

        log.debug("Monitor {} is in maintenance window, will not process notifications", monitor.getId());
    }

    private boolean isMaintenanceWindow(LocalDateTime maintenanceStart, LocalDateTime maintenanceEnd) {
        if (maintenanceStart == null || maintenanceEnd == null) {
            return false;
        }

        return LocalDateTime.now().isAfter(maintenanceStart) && LocalDateTime.now().isBefore(maintenanceEnd);
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