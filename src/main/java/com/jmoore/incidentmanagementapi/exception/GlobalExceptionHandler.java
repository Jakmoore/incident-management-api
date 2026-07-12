package com.jmoore.incidentmanagementapi.exception;

import com.jmoore.incidentmanagementapi.model.api.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleUnknown(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(exception = {MonitorNotFoundException.class, IncidentNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleMonitorNotFound(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(404).body(new ApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCutoffException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCutoffException(InvalidCutoffException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ex.getMessage()));
    }
}