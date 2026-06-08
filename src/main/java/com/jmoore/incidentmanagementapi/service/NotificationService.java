package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.config.properties.NotificationRestProperties;
import com.jmoore.incidentmanagementapi.model.notification.FailureNotification;
import com.jmoore.incidentmanagementapi.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRestProperties properties;
    private final RestClient restClient;

    public void raiseSuccessNotification(Notification notification) {
        restClient.post()
                .uri(properties.getUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(notification)
                .retrieve()
                .toBodilessEntity();
    }

    public void raiseFailureNotifications(FailureNotification failureNotification) {

    }
}
