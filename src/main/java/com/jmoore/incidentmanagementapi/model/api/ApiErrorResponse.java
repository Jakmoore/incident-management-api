package com.jmoore.incidentmanagementapi.model.api;

import java.io.Serializable;

public record ApiErrorResponse(String message) implements Serializable {
}
