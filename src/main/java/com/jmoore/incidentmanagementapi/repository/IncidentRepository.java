package com.jmoore.incidentmanagementapi.repository;

import com.jmoore.incidentmanagementapi.model.entity.incident.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByMonitorId(long monitorId);

    List<Incident> findByMonitorIdAndOpenIncidentTrue(long monitorId);

    List<Incident> findByOpenIncidentTrue();

    Optional<Incident> findTopByFingerprintAndOpenIncidentTrueOrderByCreatedAtDesc(String fingerprint);

    Optional<Incident> findTopByMonitorIdAndOpenIncidentTrue(long monitorId);

    long countByMonitorIdAndOpenIncidentTrue(long monitorId);
}
