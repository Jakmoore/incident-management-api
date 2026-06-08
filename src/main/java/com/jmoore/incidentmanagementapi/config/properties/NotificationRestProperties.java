package com.jmoore.incidentmanagementapi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "notification.rest")
public class NotificationRestProperties {

    private String url;
}
