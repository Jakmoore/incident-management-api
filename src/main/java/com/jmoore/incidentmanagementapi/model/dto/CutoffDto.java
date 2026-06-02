package com.jmoore.incidentmanagementapi.model.dto;

import com.jmoore.incidentmanagementapi.exception.InvalidCutoffException;
import lombok.Getter;

@Getter
public enum CutoffDto {

    ONE_HOUR("1h"),
    TWENTY_FOUR_HOURS("24h"),
    SEVEN_DAYS("7d"),
    THIRTY_DAYS("30d"),
    ALL_TIME("all");

    private final String value;

    CutoffDto(String value) {
        this.value = value;
    }

    public static CutoffDto from(String from) {
        for (CutoffDto cutoffDto : values()) {
            if (cutoffDto.value.equalsIgnoreCase(from)) {
                return cutoffDto;
            }
        }

        throw new InvalidCutoffException(from);
    }
}
