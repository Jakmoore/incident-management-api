package com.jmoore.incidentmanagementapi.exception;

public class MonitorNotFoundException extends RuntimeException {

    public MonitorNotFoundException(long id) {
        super("Monitor not found for ID: " + id);
    }
}
