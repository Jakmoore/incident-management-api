package com.jmoore.incidentmanagementapi.mapper;

import com.jmoore.incidentmanagementapi.model.dto.MonitorRequestDto;
import com.jmoore.incidentmanagementapi.model.dto.MonitorResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MonitorMapper {

    Monitor toEntity(MonitorRequestDto request);

    MonitorResponseDto toResponse(Monitor monitor);

    void updateEntityFromDto(MonitorRequestDto requestDto, @MappingTarget Monitor entity);
}
