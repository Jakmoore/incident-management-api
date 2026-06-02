package com.jmoore.incidentmanagementapi.exception;

public class InvalidCutoffException extends RuntimeException {

    public InvalidCutoffException(String value) {
        super(value + " is not a valid cutoff");
    }
}
