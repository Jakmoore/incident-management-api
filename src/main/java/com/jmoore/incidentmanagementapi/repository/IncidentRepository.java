package com.jmoore.incidentmanagementapi.repository;

import com.jmoore.incidentmanagementapi.model.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByMonitorId(long monitorId);
}
