package com.jmoore.incidentmanagementapi.controller;

import com.jmoore.incidentmanagementapi.model.dto.IncidentResponseDto;
import com.jmoore.incidentmanagementapi.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incidents")
@Tag(name = "Incidents Controller")
public class IncidentController {

    private final IncidentService incidentService;

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get incidents by monitor ID", description = "If only open incidents are required, set openOnly to true")
    @GetMapping(value = "/{monitorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IncidentResponseDto>> getByMonitorId(@PathVariable Long monitorId, @RequestParam(required = false) Boolean openOnly) {
        return ResponseEntity.ok(incidentService.getIncidentsByMonitorId(monitorId, openOnly));
    }
}
