package com.jmoore.incidentmanagementapi.controller;

import com.jmoore.incidentmanagementapi.service.MonitorExecutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/monitors")
@Tag(name = "Monitor Controller")
public class MonitorController {

    private final MonitorExecutorService executerService;

    @Operation(summary = "Execute health check for monitor ID")
    @GetMapping("/{monitorId}")
    public ResponseEntity<Void> runMonitor(@PathVariable Long monitorId) {
        log.info("Executing manual health check for monitor ID: {}", monitorId);

        executerService.runMonitor(monitorId);
        return ResponseEntity.ok().build();
    }
}
