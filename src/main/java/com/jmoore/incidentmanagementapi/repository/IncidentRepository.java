package com.jmoore.incidentmanagementapi.repository;

import com.jmoore.incidentmanagementapi.model.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByMonitorId(long monitorId);

    Optional<Incident> findTopByFingerprintAndOpenIncidentTrueOrderByCreatedAtDesc(String fingerprint);

    Optional<Incident> findTopByMonitorIdAndOpenIncidentTrue(long monitorId);
}
