package com.jmoore.incidentmanagementapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncidentResponseDto {

    private String url;
    private String incidentType;
    private Integer expectedStatus;
    private Integer actualStatus;
    private String failureReason;
    private String callbackEmail;
    private LocalDateTime createdAt;
    private Boolean openIncident;
    private LocalDateTime resolvedAt;
}
