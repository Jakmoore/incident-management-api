package com.jmoore.incidentmanagementapi.model.dto.dashboard;

public record DashboardResponseDto(
        int totalMonitors,
        int totalIncidents,
        int openIncidents,
        int closedIncidents
) {
}
