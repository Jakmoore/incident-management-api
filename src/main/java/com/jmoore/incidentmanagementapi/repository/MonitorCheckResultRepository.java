package com.jmoore.incidentmanagementapi.repository;

import com.jmoore.incidentmanagementapi.model.entity.MonitorCheckResult;
import com.jmoore.incidentmanagementapi.model.entity.MonitorMetricsPartial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorCheckResultRepository extends JpaRepository<MonitorCheckResult, Long> {

    @Query(
            value = """
                    SELECT
                        COUNT(*) AS totalChecks,
                        SUM(success) AS successfulChecks,
                        AVG(latency_ms) AS averageLatencyMs
                    FROM monitor_check_results
                    WHERE monitor_id = :monitorId
                    """,
            nativeQuery = true
    )
    MonitorMetricsPartial getMetrics(@Param("monitorId") long monitorId, @Param("cutoff") String cutoff);
}
