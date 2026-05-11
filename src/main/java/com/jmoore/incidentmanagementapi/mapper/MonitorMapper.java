package com.jmoore.incidentmanagementapi.mapper;

import com.jmoore.incidentmanagementapi.dto.CreateMonitorRequestDto;
import com.jmoore.incidentmanagementapi.dto.MonitorResponseDto;
import com.jmoore.incidentmanagementapi.entity.Monitor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreateMonitorMapper {

    Monitor toEntity(CreateMonitorRequestDto request);
    MonitorResponseDto toResponse(Monitor monitor);
}
