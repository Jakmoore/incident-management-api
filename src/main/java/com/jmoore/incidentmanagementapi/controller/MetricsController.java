package com.jmoore.incidentmanagementapi.controller;

import com.jmoore.incidentmanagementapi.model.dto.CutoffDto;
import com.jmoore.incidentmanagementapi.model.dto.MetricsResponseDto;
import com.jmoore.incidentmanagementapi.service.MonitorCheckResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Metrics Controller")
@RequestMapping("/api/metrics/monitors")
public class MetricsController {

    private final MonitorCheckResultService monitorCheckResultService;

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get metrics for monitor ID")
    @GetMapping(value = "/{monitorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MetricsResponseDto> getMonitorMetrics(@PathVariable long monitorId, @RequestParam(defaultValue = "30d") String cutoff) {
        CutoffDto cutoffDto = CutoffDto.from(cutoff);

        return ResponseEntity.ok(monitorCheckResultService.getMetrics(monitorId, cutoffDto));
    }
}
