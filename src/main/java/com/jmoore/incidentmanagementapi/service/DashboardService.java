package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.dto.dashboard.DashboardResponseDto;
import com.jmoore.incidentmanagementapi.model.dto.incident.IncidentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MonitorService monitorService;
    private final IncidentService incidentService;

    public DashboardResponseDto getDashboardInformation(String clientId) {
        String[] tags = {"*"};
        int totalMonitors = monitorService.getByTags(tags, clientId).size();

        List<IncidentResponseDto> incidents = incidentService.getIncidentsFiltered(null, false, clientId);
        long totalIncidents = incidents.size();
        long openIncidents = incidents.stream().filter(incident -> Boolean.TRUE.equals(incident.getOpenIncident())).count();
        long closedIncidents = totalIncidents - openIncidents;

        return new DashboardResponseDto(totalMonitors, (int) totalIncidents, (int) openIncidents, (int) closedIncidents);
    }
}
