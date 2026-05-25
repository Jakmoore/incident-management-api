package com.jmoore.incidentmanagementapi.exception;

public class IncidentNotFoundException extends RuntimeException {

    public IncidentNotFoundException(long id) {
        super("Incident not found for ID: " + id);
    }
}
