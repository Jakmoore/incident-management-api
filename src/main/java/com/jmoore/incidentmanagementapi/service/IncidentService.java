package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.exception.MonitorNotFoundException;
import com.jmoore.incidentmanagementapi.mapper.IncidentMapper;
import com.jmoore.incidentmanagementapi.model.dto.IncidentResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.Incident;
import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import com.jmoore.incidentmanagementapi.model.notification.FailureType;
import com.jmoore.incidentmanagementapi.repository.IncidentRepository;
import com.jmoore.incidentmanagementapi.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentMapper mapper;
    private final MonitorRepository monitorRepository;
    private final IncidentRepository incidentRepository;

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
    public void processIncident(long monitorId, FailureType failureType) {
        Monitor monitor = monitorRepository.findById(monitorId)
                .orElseThrow(() -> new MonitorNotFoundException(monitorId));

        String fingerprint = generateFingerprint(
                monitorId + monitor.getUrl() + failureType.name() + monitor.getCallbackEmail());

        Optional<Incident> openIncident =
                incidentRepository.findTopByFingerprintAndOpenIncidentTrueOrderByCreatedAtDesc(fingerprint);

        if (openIncident.isEmpty()) {
            Incident incident = Incident.builder()
                    .monitor(monitor)
                    .incidentType(failureType.name())
                    .expectedStatus(monitor.getExpectedStatus())
                    .url(monitor.getUrl())
                    .callbackEmail(monitor.getCallbackEmail())
                    .createdAt(LocalDateTime.now())
                    .fingerprint(fingerprint)
                    .openIncident(true)
                    .build();

            incidentRepository.save(incident);
        }
    }

    public List<IncidentResponseDto> getIncidentsByMonitorId(long monitorId) {
        log.info("Processing get incidents request for monitor ID: {}", monitorId);

        return incidentRepository.findByMonitorId(monitorId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void resolveLast(long monitorId) {
        Optional<Incident> incident = incidentRepository.findTopByMonitorIdAndOpenIncidentTrue(monitorId);

        incident.ifPresent(value -> resolveIncident(incident.get()));
    }

    @Transactional
    private void resolveIncident(Incident incident) {
        log.info("Resolving incident for monitor ID: {}", incident.getMonitor().getId());

        incident.setOpenIncident(false);
        incident.setResolvedAt(LocalDateTime.now());
        incidentRepository.save(incident);
    }

    private String generateFingerprint(String toEncode) {
        return DigestUtils.sha256Hex(toEncode);
    }
}
