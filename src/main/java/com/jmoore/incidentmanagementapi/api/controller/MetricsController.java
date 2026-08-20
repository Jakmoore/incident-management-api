package com.jmoore.incidentmanagementapi.api.controller;

import com.jmoore.incidentmanagementapi.model.dto.metrics.AverageMonitorMetricsResponseDto;
import com.jmoore.incidentmanagementapi.model.dto.metrics.MetricsResponseDto;
import com.jmoore.incidentmanagementapi.service.MetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Metrics Controller")
@RequestMapping("/api/metrics/monitors")
public class MetricsController {

    private final MetricsService metricsService;

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get average monitor metrics for Client ID")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AverageMonitorMetricsResponseDto> getAverageMonitorMetrics(@RequestHeader("clientId") String clientId) {
        log.info("Get average monitor metrics for client: {}", clientId);
        return ResponseEntity.ok(metricsService.getAverageMonitorMetrics(clientId));
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get metrics for monitor ID")
    @GetMapping(
            value = "/{monitorId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MetricsResponseDto> getMonitorMetricsForMonitorId(
            @PathVariable(name = "monitorId") long monitorId,
            @RequestParam(name = "cutoffDate", required = false) LocalDate cutoffDate,
            @RequestHeader("clientId") String clientId) {
        log.info("Getting metrics for monitor ID {}, client: {}", monitorId, clientId);
        LocalDateTime cutoffDateTime = cutoffDate == null ? LocalDateTime.now() : cutoffDate.plusDays(1).atStartOfDay();

        return ResponseEntity.ok(metricsService.getMetrics(monitorId, cutoffDateTime));
    }
}
