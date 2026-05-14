package com.jmoore.incidentmanagementapi.controller;

import com.jmoore.incidentmanagementapi.model.dto.IncidentResponseDto;
import com.jmoore.incidentmanagementapi.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Incidents Controller")
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get incidents by monitor ID")
    @GetMapping(value = "/{monitorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IncidentResponseDto>> getByMonitorId(@PathVariable Long monitorId) {
        List<IncidentResponseDto> incidents = incidentService.getIncidentsByMonitorId(monitorId);

        return ResponseEntity.ok(incidents);
    }
}
