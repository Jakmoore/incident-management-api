package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.api.HealthCheckResult;
import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import com.jmoore.incidentmanagementapi.model.notification.FailureType;
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

    /**
     * Executes the health check GET call against the monitor. The monitor is expected to expose
     * an appropriate endpoint for its health check.
     * <p>
     * The request will retry 3 times for a ResourceAccessException to allow the service time
     * to recover from a short network issue. Otherwise, the failure is handled in
     * {@link #recover(ResourceAccessException, Monitor)}.
     *
     * @param monitor to execute the health check against
     * @return the result
     */
    @Retryable(retryFor = ResourceAccessException.class, backoff = @Backoff(delay = 300))
    public HealthCheckResult executeHealthCheck(Monitor monitor) {
        ResponseEntity<Void> response = restClient.get()
                .uri(monitor.getUrl())
                .exchange((request, clientResponse) ->
                        ResponseEntity.status(clientResponse.getStatusCode()).build());

        int statusCode = response.getStatusCode().value();

        if (statusCode != monitor.getExpectedStatus()) {
            return result(false, FailureType.HTTP_STATUS_ERROR, statusCode, monitor.getExpectedStatus(), monitor.getId(), monitor.getUrl(), monitor.getCallbackUrl());
        }

        log.info("Health check successful for monitor ID: {}", monitor.getId());

        return result(true, null, statusCode, monitor.getExpectedStatus(), monitor.getId(), monitor.getUrl(), monitor.getCallbackUrl());
    }

    @Recover
    public HealthCheckResult recover(ResourceAccessException ex, Monitor monitor) {
        log.warn("Health check failed after max retry attemps for {}, raising notification", monitor.getUrl());

        if (log.isDebugEnabled()) {
            log.debug("Exception: {}", ex.getMessage());
        }

        return result(false, FailureType.NETWORK_ERROR, null, monitor.getExpectedStatus(), monitor.getId(), monitor.getUrl(), monitor.getCallbackUrl());
    }

    private HealthCheckResult result(boolean success, FailureType type, Integer actual, int expected, long monitorId, String url, String callbackUrl) {
        return new HealthCheckResult(
                success,
                type,
                actual,
                expected,
                monitorId,
                url,
                callbackUrl
        );
    }
}