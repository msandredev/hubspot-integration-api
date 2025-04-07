package br.com.msandredev.hubspotintegrationapi.config;

import br.com.msandredev.hubspotintegrationapi.domain.model.RateLimit;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hubspot.api")
public class HubSpotApiProperties {

    private String baseUrl;
    private String createContact;
    private RateLimit rateLimit;
}
