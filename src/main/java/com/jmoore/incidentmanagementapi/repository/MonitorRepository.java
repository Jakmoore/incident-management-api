package com.jmoore.incidentmanagementapi.repository;

import com.jmoore.incidentmanagementapi.model.entity.monitor.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {

    List<Monitor> findByActiveTrueAndNextRunAtBefore(LocalDateTime now);

    @Query(value =
            "SELECT * FROM public.monitors " +
                    "WHERE tags && CAST(:tags AS TEXT[])",
            nativeQuery = true)
    List<Monitor> findByTags(List<String> tags);
}
