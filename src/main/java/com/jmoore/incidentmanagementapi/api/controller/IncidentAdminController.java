package com.jmoore.incidentmanagementapi.api.controller;

import com.jmoore.incidentmanagementapi.service.IncidentService;
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
@RequestMapping("/api/admin/incidents")
@Tag(name = "Incident Admin Controller")
public class IncidentAdminController {

    private final IncidentService incidentService;

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Resolve Incident")
    @PatchMapping(
            value = "/{incidentId}/resolve",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> resolveIncident(
            @PathVariable("incidentId") Long incidentId,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing resolve incident request for client: {}", clientId);

        incidentService.resolveIncidentById(incidentId);

        return ResponseEntity.ok().build();
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Delete Incident")
    @DeleteMapping(
            value = "/{incidentId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteIncident(
            @PathVariable("incidentId") Long incidentId,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing delete incident request for client: {}", clientId);

        incidentService.deleteIncident(incidentId);

        return ResponseEntity.ok().build();
    }
}
