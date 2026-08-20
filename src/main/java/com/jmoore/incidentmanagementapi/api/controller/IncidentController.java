package com.jmoore.incidentmanagementapi.api.controller;

import com.jmoore.incidentmanagementapi.model.dto.incident.IncidentResponseDto;
import com.jmoore.incidentmanagementapi.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incidents")
@Tag(name = "Incidents Controller")
public class IncidentController {

    private final IncidentService incidentService;

    @ApiResponse(responseCode = "200")
    @Operation(
            summary = "Get incidents filtered by client ID, monitor ID and open only",
            description = "If only open incidents are required, set openOnly to true")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IncidentResponseDto>> getIncidents(
            @RequestParam(name = "monitorId", required = false) Long monitorId,
            @RequestParam(name = "openOnly", required = false) Boolean openOnly,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing get incidents request for client: {}", clientId);

        return ResponseEntity.ok(incidentService.getIncidentsFiltered(monitorId, openOnly, clientId));
    }
}
