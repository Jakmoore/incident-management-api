package com.jmoore.incidentmanagementapi.repository;

import com.jmoore.incidentmanagementapi.model.entity.monitor.MonitorCheckResult;
import com.jmoore.incidentmanagementapi.model.entity.metrics.MonitorMetricsPartial;
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
}
