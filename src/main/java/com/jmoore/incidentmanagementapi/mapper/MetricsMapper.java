package com.jmoore.incidentmanagementapi.mapper;

import com.jmoore.incidentmanagementapi.model.dto.metrics.AverageMonitorMetricsResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.metrics.AverageMonitorMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MetricsMapper {

    AverageMonitorMetricsResponseDto toResponse(AverageMonitorMetrics metrics);
}
