package com.jmoore.incidentmanagementapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckExecuter {

    private final RestClient restClient;
    private final NotificationService notificationService;

    @Async
    public void executeHealthCheck(String url, int expectedStatusCode) {
        try {
            ResponseEntity<Void> response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity();

            if (response.getStatusCode() != HttpStatusCode.valueOf(expectedStatusCode)) {
                log.warn("Healthcheck for monitor {} failed. Raising notification", url);
                notificationService.raiseNotification();
            }
        } catch (Exception e) {
            log.warn("Healthcheck for monitor {} failed. Raising notification", url);
            notificationService.raiseNotification();
        }
    }
}
