package com.jmoore.incidentmanagementapi.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMonitorRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String url;

    private int expectedStatus;
    private int intervalSeconds;

    @Email
    @NotBlank
    private String callbackEmail;
}
