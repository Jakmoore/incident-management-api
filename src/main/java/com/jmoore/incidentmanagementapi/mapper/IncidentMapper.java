package com.jmoore.incidentmanagementapi.mapper;

import com.jmoore.incidentmanagementapi.model.dto.incident.IncidentResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.incident.Incident;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncidentMapper {

    IncidentResponseDto toResponse(Incident incident);
}
