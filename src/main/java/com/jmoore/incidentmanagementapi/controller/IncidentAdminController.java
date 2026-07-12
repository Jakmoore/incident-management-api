package com.jmoore.incidentmanagementapi.controller;

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
@RequestMapping("/api/admin/incidents")
@Tag(name = "Incident Admin Controller")
public class IncidentAdminController {

    private final IncidentService incidentService;

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get All Incidents")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IncidentResponseDto>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Retrieve All Open Incidents")
    @GetMapping(value = "/open", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IncidentResponseDto>> getOpenIncidents() {
        return ResponseEntity.ok(incidentService.getOpenIncidents());
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Resolve Incident")
    @PatchMapping(value = "/{incidentId}/resolve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> resolveIncident(@PathVariable long incidentId) {
        incidentService.resolveIncidentById(incidentId);

        return ResponseEntity.ok().build();
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Delete Incident")
    @DeleteMapping(value = "/{incidentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteIncident(@PathVariable long incidentId) {
        incidentService.deleteIncident(incidentId);

        return ResponseEntity.ok().build();
    }
}
