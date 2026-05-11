package com.jmoore.incidentmanagementapi.exception;

public class MonitorNotFoundException extends RuntimeException {
  public MonitorNotFoundException(String message) {
    super(message);
  }
}
