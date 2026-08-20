package com.jmoore.incidentmanagementapi.repository;

import com.jmoore.incidentmanagementapi.model.entity.monitor.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<Monitor> findByTags(@Param("tags") List<String> tags);

    List<Monitor> findByClientId(@Param("clientId") String clientId);

    @Query(value =
            "SELECT * FROM public.monitors " +
                    "WHERE tags && CAST(:tags AS TEXT[]) " +
                    "AND client_id = :clientId",
            nativeQuery = true)
    List<Monitor> findByTagsAndClientId(@Param("tags") String[] tags, @Param("clientId") String clientId);
}
