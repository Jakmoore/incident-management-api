package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.exception.IncidentNotFoundException;
import com.jmoore.incidentmanagementapi.mapper.IncidentMapper;
import com.jmoore.incidentmanagementapi.model.dto.incident.IncidentResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.incident.Incident;
import com.jmoore.incidentmanagementapi.model.entity.monitor.Monitor;
import com.jmoore.incidentmanagementapi.model.notification.FailureType;
import com.jmoore.incidentmanagementapi.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final MonitorService monitorService;
    private final IncidentFingerprintGenerator fingerprintGenerator;
    private final IncidentRepository incidentRepository;
    private final IncidentMapper mapper;

    /**
     * Creates an incident for a monitor failure if no active incident already exists for the same
     * failure fingerprint.
     * <p>
     * A fingerprint is generated from the monitor id, URL, failure type, and callback email to
     * uniquely identify a logical failure scenario. This is used to prevent duplicate open incidents
     * for the same ongoing issue.
     * <p>
     * If an open incident already exists with the same fingerprint, no new incident is created.
     *
     * @param monitorId   the id of the monitor that failed
     * @param failureType the type of failure detected (e.g. network error, HTTP status mismatch)
     */
    @Transactional
    public boolean processIncident(long monitorId, FailureType failureType, Integer actualStatus) {
        Monitor monitor = monitorService.getEntityById(monitorId);

        String fingerprint = fingerprintGenerator.generate(
                monitorId + monitor.getUrl() + failureType.name() + monitor.getCallbackEmail());

        Optional<Incident> openIncident = getLastOpenIncidentByFingerprint(fingerprint);

        if (openIncident.isEmpty()) {
            Incident incident = Incident.builder()
                    .monitor(monitor)
                    .incidentType(failureType.name())
                    .expectedStatus(monitor.getExpectedStatus())
                    .actualStatus(actualStatus)
                    .url(monitor.getUrl())
                    .callbackEmail(monitor.getCallbackEmail())
                    .createdAt(LocalDateTime.now())
                    .fingerprint(fingerprint)
                    .openIncident(true)
                    .build();

            incidentRepository.save(incident);

            return true;
        }

        return false;
    }

    public List<IncidentResponseDto> getIncidentsFiltered(Long monitorId, Boolean openOnly, String clientId) {
        List<Incident> incidents = monitorId == null ?
                incidentRepository.findByMonitor_ClientId(clientId) :
                incidentRepository.findByMonitor_ClientIdAndMonitor_Id(clientId, monitorId);

        if (Boolean.TRUE.equals(openOnly)) {
            incidents = incidents.stream()
                    .filter(Incident::getOpenIncident).toList();
        }

        return incidents.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void resolveIncidentById(Long incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IncidentNotFoundException(incidentId));

        resolveIncident(incident);
    }

    @Transactional
    public void resolveIncident(Incident incident) {
        log.info("Resolving incident for monitor ID: {}", incident.getMonitor().getId());

        incident.setOpenIncident(false);
        incident.setResolvedAt(LocalDateTime.now());
    }

    @Transactional
    public void deleteIncident(Long incidentId) {
        incidentRepository.deleteById(incidentId);
    }

    public Optional<Incident> getLastOpenIncidentByFingerprint(String fingerprint) {
        return incidentRepository.findTopByFingerprintAndOpenIncidentTrueOrderByCreatedAtDesc(fingerprint);
    }

    public Long getOpenIncidentCount(Long monitorId) {
        return incidentRepository.countByMonitorIdAndOpenIncidentTrue(monitorId);
    }
}