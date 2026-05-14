package com.jmoore.incidentmanagementapi.controller;

import com.jmoore.incidentmanagementapi.model.dto.MonitorRequestDto;
import com.jmoore.incidentmanagementapi.model.dto.MonitorResponseDto;
import com.jmoore.incidentmanagementapi.service.MonitorService;
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
@RequestMapping("/api/admin/monitors")
@Tag(name = "Monitor Admin Controller")
public class MonitorAdminController {

    private final MonitorService monitorService;

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Add new monitor")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitorResponseDto> createMonitor(@RequestBody MonitorRequestDto createMonitorRequest) {
        try {
            MonitorResponseDto created = monitorService.createMonitor(createMonitorRequest);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get all monitors")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonitorResponseDto>> getAllMonitors() {
        try {
            List<MonitorResponseDto> monitors = monitorService.getAll();
            return ResponseEntity.ok(monitors);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get monitor by ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitorResponseDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(monitorService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Enable monitor")
    @PatchMapping(value = "/{id}/enable")
    public ResponseEntity<MonitorResponseDto> enableMonitor(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(monitorService.enableMonitor(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Disable monitor")
    @PatchMapping(value = "/{id}/disable")
    public ResponseEntity<MonitorResponseDto> disableMonitor(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(monitorService.disableMonitor(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Update monitor configuration")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitorResponseDto> updateMonitorConfiguration(@PathVariable Long id, @RequestBody MonitorRequestDto request) {
        try {
            return ResponseEntity.ok(monitorService.updateMonitorConfiguration(id, request));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Delete monitor")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMonitor(@PathVariable Long id) {
        try {
            monitorService.deleteMonitor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
