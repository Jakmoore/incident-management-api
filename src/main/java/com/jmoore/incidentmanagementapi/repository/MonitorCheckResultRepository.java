package com.jmoore.incidentmanagementapi.repository;

import com.jmoore.incidentmanagementapi.model.entity.metrics.AverageMonitorMetrics;
import com.jmoore.incidentmanagementapi.model.entity.metrics.MonitorMetricsPartial;
import com.jmoore.incidentmanagementapi.model.entity.monitor.MonitorCheckResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MonitorCheckResultRepository extends JpaRepository<MonitorCheckResult, Long> {

    @Query(
            value = """
                    SELECT
                        COUNT(*) AS totalChecks,
                        COUNT(*) FILTER (WHERE success) AS successfulChecks,
                        AVG(latency_ms) AS averageLatencyMs
                    FROM monitor_check_results
                    WHERE monitor_id = :monitorId
                    AND created_at < :cutoff
                    """,
            nativeQuery = true
    )
    MonitorMetricsPartial getMetrics(@Param("monitorId") long monitorId, @Param("cutoff") LocalDateTime cutoffDateTime);

    @Query(
            value = """
                    SELECT
                        COUNT(DISTINCT m.id) AS totalMonitors,
                        COUNT(mc.id) AS totalChecks,
                        COUNT(*) FILTER (WHERE mc.success = true) AS successfulChecks,
                        COUNT(*) FILTER (WHERE mc.success = false) AS failedChecks,
                        ROUND(
                            COUNT(*) FILTER (WHERE mc.success = true) * 100.0
                            / NULLIF(COUNT(mc.id), 0),
                            2
                        ) AS uptimePercentage,
                        (
                            SELECT COUNT(*)
                            FROM incidents i
                            JOIN monitors im
                                ON im.id = i.monitor_id
                            WHERE im.client_id = :clientId
                        ) AS incidents
                    FROM monitors m
                    LEFT JOIN monitor_check_results mc
                        ON mc.monitor_id = m.id
                    WHERE m.client_id = :clientId
                    """, nativeQuery = true
    )
    AverageMonitorMetrics getAverageMonitorMetrics(@Param("clientId") String clientId);
}
