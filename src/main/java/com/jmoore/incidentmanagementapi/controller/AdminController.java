package com.jmoore.incidentmanagementapi.controller;

import com.jmoore.incidentmanagementapi.model.dto.CreateMonitorRequestDto;
import com.jmoore.incidentmanagementapi.model.dto.MonitorResponseDto;
import com.jmoore.incidentmanagementapi.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/monitors")
public class MonitorController {

    private final MonitorService monitorService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitorResponseDto> createMonitor(@RequestBody CreateMonitorRequestDto createMonitorRequest) {
        try {
            MonitorResponseDto created = monitorService.createMonitor(createMonitorRequest);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonitorResponseDto>> getAllMonitors() {
        try {
            List<MonitorResponseDto> monitors = monitorService.getAll();
            return ResponseEntity.ok(monitors);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitorResponseDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(monitorService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
