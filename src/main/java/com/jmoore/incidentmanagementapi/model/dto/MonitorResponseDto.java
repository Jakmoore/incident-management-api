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
public class MonitorResponseDto {

    private Long id;
    private String name;
    private String url;
    private int expectedStatus;
    private int intervalSeconds;
    private boolean active;
    private LocalDateTime createdAt;
}
