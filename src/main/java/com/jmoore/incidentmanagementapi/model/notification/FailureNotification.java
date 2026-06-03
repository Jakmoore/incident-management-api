package com.jmoore.incidentmanagementapi.model.notification;

import lombok.Getter;

@Getter
public class FailureNotification extends Notification {

    private final FailureType failureType;

    public FailureNotification(String url, int expectedStatusCode, Integer actualStatusCode, String callbackEmail, FailureType failureType) {
        super(url, expectedStatusCode, actualStatusCode, callbackEmail);
        this.failureType = failureType;
    }
}
