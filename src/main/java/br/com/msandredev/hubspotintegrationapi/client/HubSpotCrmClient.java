package br.com.msandredev.hubspotintegrationapi.client;

import br.com.msandredev.hubspotintegrationapi.config.HubSpotApiProperties;
import br.com.msandredev.hubspotintegrationapi.dto.contact.HubSpotContactRequest;
import br.com.msandredev.hubspotintegrationapi.dto.contact.HubSpotContactResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hubspot-crm-client", url = "${hubspot.api.base-url}", configuration = HubSpotApiProperties.class
)
public interface HubSpotCrmClient {

    @PostMapping(value = "${hubspot.api.create-contact}", consumes = "application/json")
    HubSpotContactResponse createContact(
            @RequestBody HubSpotContactRequest contact,
            @RequestHeader("Authorization") String accessToken
    );
}
