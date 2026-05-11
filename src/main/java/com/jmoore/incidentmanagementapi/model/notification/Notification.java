package com.jmoore.incidentmanagementapi.model.notification;

public record Notification(FailureType failureType, String url, int expectedStatusCode, Integer actualStatusCode, String callbackUrl) {
}
