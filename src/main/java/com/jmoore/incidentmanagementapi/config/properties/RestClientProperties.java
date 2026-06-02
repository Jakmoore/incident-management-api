package com.jmoore.incidentmanagementapi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "rest.client")
public class RestClientProperties {

    private int connectTimeout = 3000;
    private int readTimeout = 3000;
}
