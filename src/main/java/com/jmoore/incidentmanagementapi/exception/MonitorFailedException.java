package com.jmoore.incidentmanagementapi.exception;

public class MonitorFailedException extends RuntimeException {
  public MonitorFailedException(String message) {
    super(message);
  }
}
