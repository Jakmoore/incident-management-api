package com.jmoore.incidentmanagementapi.api.controller;

import com.jmoore.incidentmanagementapi.api.validation.TagsValidator;
import com.jmoore.incidentmanagementapi.exception.InvalidTagsException;
import com.jmoore.incidentmanagementapi.model.dto.monitor.MonitorRequestDto;
import com.jmoore.incidentmanagementapi.model.dto.monitor.MonitorResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.monitor.MaintenanceWindow;
import com.jmoore.incidentmanagementapi.service.MonitorService;
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
@RequestMapping("/api/admin/monitors")
@Tag(name = "Monitor Admin Controller")
public class MonitorAdminController {

    private final TagsValidator tagsValidator;
    private final MonitorService monitorService;

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Add new monitor")
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MonitorResponseDto> createMonitor(
            @RequestBody MonitorRequestDto createMonitorRequest,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing create monitor request for client: {}", clientId);

        MonitorResponseDto created = monitorService.createMonitor(createMonitorRequest, clientId);

        return ResponseEntity.ok(created);
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get monitors by tags")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonitorResponseDto>> getMonitorsByTags(
            @RequestParam("tags") String[] tags,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing get monitors by tags request for client: {}", clientId);

        if (!tagsValidator.isValid(tags)) {
            throw new InvalidTagsException("Invalid tags provided");
        }

        return ResponseEntity.ok(monitorService.getByTags(tags, clientId));
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get monitor by ID")
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MonitorResponseDto> getById(
            @PathVariable("id") Long id,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing get monitor request for client: {}", clientId);

        return ResponseEntity.ok(monitorService.getById(id));
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Enable monitor")
    @PatchMapping(value = "/{id}/enable")
    public ResponseEntity<MonitorResponseDto> enableMonitor(
            @PathVariable("id") Long id,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing enable monitor request for client {}", clientId);

        return ResponseEntity.ok(monitorService.enableMonitor(id));
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Disable monitor")
    @PatchMapping(value = "/{id}/disable")
    public ResponseEntity<MonitorResponseDto> disableMonitor(
            @PathVariable("id") Long id,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing disable monitor request for client {}", clientId);

        return ResponseEntity.ok(monitorService.disableMonitor(id));
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Update monitor configuration")
    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitorResponseDto> updateMonitorConfiguration(
            @PathVariable("id") Long id,
            @RequestBody MonitorRequestDto request,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing update monitor configuration for client: {}", clientId);

        return ResponseEntity.ok(monitorService.updateMonitorConfiguration(id, request));
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Delete monitor")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMonitor(
            @PathVariable("id") Long id,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing delete monitor request for client: {}", clientId);

        monitorService.deleteMonitor(id, clientId);

        return ResponseEntity.noContent().build();
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Activate Maintenance Window")
    @PostMapping(
            value = "/{id}/maintenance",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitorResponseDto> activateMaintenanceWindow(
            @PathVariable("id") Long id,
            @RequestBody MaintenanceWindow maintenanceWindow,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing activate maintenance window for client: {}", clientId);

        return ResponseEntity.ok(monitorService.activateMaintenanceWindow(id, maintenanceWindow));
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Deactivate Maintenance Window")
    @DeleteMapping(
            value = "/{id}/maintenance",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitorResponseDto> deactivateMaintenanceWindow(
            @PathVariable("id") Long id,
            @RequestHeader("clientId") String clientId) {
        log.info("Processing deactivate maintenance window for client: {}", clientId);

        return ResponseEntity.ok(monitorService.deactivateMaintenanceWindow(id));
    }
}