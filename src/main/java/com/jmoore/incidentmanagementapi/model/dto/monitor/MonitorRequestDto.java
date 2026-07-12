package com.jmoore.incidentmanagementapi.model.dto.monitor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonitorRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String url;

    private int expectedStatus;
    private int intervalSeconds;

    @Email
    @NotBlank
    private String callbackEmail;

    private List<String> tags;
    private MaintenanceWindowDto maintenanceWindow;
    private long expectedLatency;
}
