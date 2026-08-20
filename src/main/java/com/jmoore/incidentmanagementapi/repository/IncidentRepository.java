package com.jmoore.incidentmanagementapi.repository;

import com.jmoore.incidentmanagementapi.model.entity.incident.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByMonitor_ClientId(String clientId);

    List<Incident> findByMonitor_ClientIdAndMonitor_Id(String clientId, Long monitorId);

    Optional<Incident> findTopByFingerprintAndOpenIncidentTrueOrderByCreatedAtDesc(String fingerprint);

    long countByMonitorIdAndOpenIncidentTrue(Long monitorId);
}
