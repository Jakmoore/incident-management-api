package com.jmoore.incidentmanagementapi.controller;

import com.jmoore.incidentmanagementapi.model.dto.dashboard.DashboardResponseDto;
import com.jmoore.incidentmanagementapi.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard Controller")
public class DashboardController {

    private final DashboardService dashboardService;

    @ApiResponse(responseCode = "200")
    @Operation(summary = "Get Dashboard Information")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardResponseDto> getDashboardInformation() {
        return ResponseEntity.ok(dashboardService.getDashboardInformation());
    }
}
