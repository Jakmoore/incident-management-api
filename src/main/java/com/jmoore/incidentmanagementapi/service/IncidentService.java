package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.mapper.IncidentMapper;
import com.jmoore.incidentmanagementapi.model.dto.IncidentResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.Incident;
import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import com.jmoore.incidentmanagementapi.model.notification.FailureType;
import com.jmoore.incidentmanagementapi.repository.IncidentRepository;
import com.jmoore.incidentmanagementapi.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.management.monitor.MonitorSettingException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentMapper mapper;
    private final MonitorRepository monitorRepository;
    private final IncidentRepository incidentRepository;

    public void createIncident(long monitorId, FailureType failureType) {
        Monitor monitor = monitorRepository.findById(monitorId)
                .orElseThrow(MonitorSettingException::new);

        Incident incident = Incident.builder()
                .monitor(monitor)
                .incidentType(failureType.name())
                .expectedStatus(monitor.getExpectedStatus())
                .url(monitor.getUrl())
                .callbackUrl(monitor.getCallbackUrl())
                .createdAt(LocalDateTime.now())
                .build();

        incidentRepository.save(incident);
    }

    public List<IncidentResponseDto> getIncidentsByMonitorId(long monitorId) {
        log.info("Processing get incidents request for monitor ID: {}", monitorId);
        return incidentRepository.findByMonitorId(monitorId).stream().map(mapper::toResponse).toList();
    }
}
