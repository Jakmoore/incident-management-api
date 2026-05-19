package com.jmoore.incidentmanagementapi.config;

import com.jmoore.incidentmanagementapi.config.properties.RestClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final RestClientProperties restClientProperties;

    @Bean
    public RestClient restClient() {
        return RestClient.builder().
                requestFactory(clientHttpRequestFactory())
                .build();
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(restClientProperties.getConnectTimeout());
        clientHttpRequestFactory.setReadTimeout(restClientProperties.getReadTimeout());

        return clientHttpRequestFactory;
    }
}
