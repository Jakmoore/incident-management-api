package com.jmoore.incidentmanagementapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMonitorRequestDto {

    private String name;
    private String url;
    private int expectedStatus;
    private int intervalSeconds;
    private String callbackUrl;
}
