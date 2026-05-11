package com.jmoore.incidentmanagementapi.repository;

import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {

    List<Monitor> findByActiveTrueAndNextRunAtBefore(LocalDateTime now);
}
