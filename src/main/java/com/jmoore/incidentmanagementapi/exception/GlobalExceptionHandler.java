package com.jmoore.incidentmanagementapi.exception;

import com.jmoore.incidentmanagementapi.model.api.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleUnknown(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(exception = {
            MonitorNotFoundException.class,
            IncidentNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(exception = {InvalidTagsException.class, InvalidCutoffException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<Void> handleIllegalOperation(IllegalOperationException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}