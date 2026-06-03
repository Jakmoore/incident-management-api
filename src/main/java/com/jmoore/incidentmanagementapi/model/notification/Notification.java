package com.jmoore.incidentmanagementapi.model.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Notification {

    private String url;
    private int expectedStatusCode;
    private Integer actualStatusCode;
    private String callbackEmail;
}
