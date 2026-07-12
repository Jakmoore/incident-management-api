package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.dto.dashboard.DashboardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MonitorService monitorService;
    private final IncidentService incidentService;

    public DashboardResponseDto getDashboardInformation() {
        int totalMonitors = monitorService.getAll(null).size();
        int totalIncidents = incidentService.getAllIncidents().size();
        int openIncidents = incidentService.getOpenIncidents().size();
        int closedIncidents = totalIncidents - openIncidents;

        return new DashboardResponseDto(totalMonitors, totalIncidents, openIncidents, closedIncidents);
    }
}
