package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.model.entity.Incident;
import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import com.jmoore.incidentmanagementapi.model.notification.FailureType;
import com.jmoore.incidentmanagementapi.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public void createIncident(Monitor monitor, FailureType failureType) {
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
}
