package br.com.msandredev.hubspotintegrationapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "hubspot")
public class HubSpotProperties {
    private String clientId;
    private String authUrl;
    private String redirectUri;
    private String[] scopes;
    private String clientSecret;
}
