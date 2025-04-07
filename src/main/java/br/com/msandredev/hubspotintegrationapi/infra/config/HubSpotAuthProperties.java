package br.com.msandredev.hubspotintegrationapi.infra.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "hubspot")
public class HubSpotAuthProperties {
    private String clientId;
    private String authUrl;
    private String redirectUri;
    private String[] scopes;
    private String clientSecret;
}
