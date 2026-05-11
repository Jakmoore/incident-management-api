package com.jmoore.incidentmanagementapi.mapper;

import com.jmoore.incidentmanagementapi.model.dto.CreateMonitorRequestDto;
import com.jmoore.incidentmanagementapi.model.dto.MonitorResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MonitorMapper {

    Monitor toEntity(CreateMonitorRequestDto request);
    MonitorResponseDto toResponse(Monitor monitor);
}
