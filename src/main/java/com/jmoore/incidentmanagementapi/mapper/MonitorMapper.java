package com.jmoore.incidentmanagementapi.mapper;

import com.jmoore.incidentmanagementapi.model.dto.monitor.MaintenanceWindowDto;
import com.jmoore.incidentmanagementapi.model.dto.monitor.MonitorRequestDto;
import com.jmoore.incidentmanagementapi.model.dto.monitor.MonitorResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.monitor.MaintenanceWindow;
import com.jmoore.incidentmanagementapi.model.entity.monitor.Monitor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MonitorMapper {

    Monitor toEntity(MonitorRequestDto request);

    MonitorResponseDto toResponse(Monitor monitor);

    MaintenanceWindow toEntity(MaintenanceWindowDto maintenanceWindow);

    MaintenanceWindowDto toDto(MaintenanceWindow maintenanceWindow);

    void updateEntityFromDto(MonitorRequestDto requestDto, @MappingTarget Monitor entity);
}
