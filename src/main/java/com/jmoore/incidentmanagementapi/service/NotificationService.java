package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.notification.Notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async
    public void raiseNotification(Notification notification) {

    }
}
