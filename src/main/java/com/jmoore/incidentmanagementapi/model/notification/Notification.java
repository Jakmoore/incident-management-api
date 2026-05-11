package com.jmoore.incidentmanagementapi.model;

public record Notification(String message, String url, int expectedStatusCode, int actualStatusCode) {
}
