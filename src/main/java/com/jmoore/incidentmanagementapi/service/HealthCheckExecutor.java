package com.jmoore.incidentmanagementapi.service;

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
    private final NotificationService notificationService;

    /**
     * Executes the health check GET call against the monitor. The monitor is expected to expose
     * and appropriate endpoint for its health check.
     * <p>
     * The request will be retried twice in the case of a ResourceAccessException.
     *
     * @param url                to execute the health check against
     * @param expectedStatusCode to compare with actual status code
     * @param callbackUrl        to call upon failure and notify of an issue
     */
    @Retryable(retryFor = ResourceAccessException.class, maxAttempts = 2, backoff = @Backoff(delay = 300))
    public void executeHealthCheck(String url, int expectedStatusCode, String callbackUrl) {
        ResponseEntity<Void> response = restClient.get().uri(url).retrieve().toBodilessEntity();

        int statusCode = response.getStatusCode().value();

        if (statusCode != expectedStatusCode) {
            raiseNotification(url, expectedStatusCode, statusCode, callbackUrl);
        }
    }

    @Recover
    public void recover(ResourceAccessException ex, String url, int expectedStatusCode, String callbackUrl) {
        log.warn("Health check failed after max retry attemps for {}, raising notification", url);

        if (log.isDebugEnabled()) {
            log.debug("Exception: {}", ex.getMessage());
        }

        notificationService.raiseNotification(new Notification(FailureType.NETWORK_ERROR, url, expectedStatusCode, null, callbackUrl));
    }

    private void raiseNotification(String url, int expectedStatusCode, Integer actualStatusCode, String callbackUrl) {
        log.warn("Healthcheck for monitor {} failed. Raising notification for callback URL: {}", url, callbackUrl);
        notificationService.raiseNotification(new Notification(FailureType.HTTP_STATUS_ERROR, url, expectedStatusCode, actualStatusCode, callbackUrl));
    }
}
