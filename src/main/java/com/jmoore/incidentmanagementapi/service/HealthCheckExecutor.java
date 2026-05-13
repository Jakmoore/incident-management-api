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
     * Executes an HTTP GET health check against the provided monitor endpoint.
     * <p>
     * The monitor is expected to expose a reachable health endpoint that returns an HTTP status code
     * representing its current state.
     * <p>
     * The request is retried up to 3 times in the event of a {@link ResourceAccessException}, which
     * typically indicates transient network issues (e.g. timeouts or connection failures). If all
     * retry attempts are exhausted, the failure is handled in
     * {@link #recover(ResourceAccessException, Monitor)}.
     *
     * @param monitor the monitor definition containing the target URL and expected response details
     * @return a {@code HealthCheckResult} representing success or failure of the health check
     */
    @Retryable(retryFor = ResourceAccessException.class, backoff = @Backoff(delay = 300))
    public HealthCheckResult executeHealthCheck(Monitor monitor) {
        ResponseEntity<Void> response = restClient.get()
                .uri(monitor.getUrl())
                .exchange((request, clientResponse) ->
                        ResponseEntity.status(clientResponse.getStatusCode()).build());

        int statusCode = response.getStatusCode().value();

        if (statusCode != monitor.getExpectedStatus()) {
            return result(
                    false,
                    FailureType.HTTP_STATUS_ERROR,
                    statusCode,
                    monitor.getExpectedStatus(),
                    monitor.getId(),
                    monitor.getUrl(),
                    monitor.getCallbackEmail());
        }

        log.info("Health check successful for monitor ID: {}", monitor.getId());

        return result(
                true,
                null,
                statusCode,
                monitor.getExpectedStatus(),
                monitor.getId(),
                monitor.getUrl(),
                monitor.getCallbackEmail());
    }

    @Recover
    public HealthCheckResult recover(ResourceAccessException ex, Monitor monitor) {
        log.warn("Health check failed after max retry attemps for {}, raising notification", monitor.getUrl());

        if (log.isDebugEnabled()) {
            log.debug("Exception: {}", ex.getMessage());
        }

        return result(
                false,
                FailureType.NETWORK_ERROR,
                null,
                monitor.getExpectedStatus(),
                monitor.getId(),
                monitor.getUrl(),
                monitor.getCallbackEmail());
    }

    private HealthCheckResult result(boolean success, FailureType type, Integer actual, int expected, long monitorId, String url, String callbackEmail) {
        return new HealthCheckResult(
                success,
                type,
                actual,
                expected,
                monitorId,
                url,
                callbackEmail
        );
    }
}