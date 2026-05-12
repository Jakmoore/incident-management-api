package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import com.jmoore.incidentmanagementapi.model.notification.FailureType;
import com.jmoore.incidentmanagementapi.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckExecutor {

    private final RestClient restClient;
    private final IncidentService incidentService;
    private final NotificationService notificationService;

    /**
     * Executes the health check GET call against the monitor. The monitor is expected to expose
     * and appropriate endpoint for its health check.
     * <p>
     * The request will be retried twice in the case of a ResourceAccessException.
     *
     * @param monitor entity representing the monitor to execute the health check against
     */
    @Retryable(retryFor = ResourceAccessException.class, maxAttempts = 2, backoff = @Backoff(delay = 300))
    public void executeHealthCheck(Monitor monitor) {
        ResponseEntity<Void> response = restClient.get().uri(monitor.getUrl()).retrieve().toBodilessEntity();

        int statusCode = response.getStatusCode().value();

        if (statusCode != monitor.getExpectedStatus()) {
            raiseNotification(monitor.getUrl(), monitor.getExpectedStatus(), statusCode, monitor.getCallbackUrl());
        }
    }

    @Recover
    public void recover(ResourceAccessException ex, Monitor monitor) {
        log.warn("Health check failed after max retry attemps for {}, raising notification", monitor.getUrl());

        if (log.isDebugEnabled()) {
            log.debug("Exception: {}", ex.getMessage());
        }

        notificationService.raiseNotification(new Notification(FailureType.NETWORK_ERROR, monitor.getUrl(), monitor.getExpectedStatus(), null, monitor.getCallbackUrl()));
        incidentService.createIncident(monitor, FailureType.NETWORK_ERROR);
    }

    private void raiseNotification(String url, int expectedStatusCode, Integer actualStatusCode, String callbackUrl) {
        log.warn("Healthcheck for monitor {} failed. Raising notification for callback URL: {}", url, callbackUrl);
        notificationService.raiseNotification(new Notification(FailureType.HTTP_STATUS_ERROR, url, expectedStatusCode, actualStatusCode, callbackUrl));
    }
}
