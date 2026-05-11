package com.jmoore.incidentmanagementapi.exception;

import com.jmoore.incidentmanagementapi.model.api.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MonitorNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleMonitorNotFound(MonitorNotFoundException ex) {
        return ResponseEntity.status(404).body(new ApiErrorResponse(ex.getMessage()));
    }
}
