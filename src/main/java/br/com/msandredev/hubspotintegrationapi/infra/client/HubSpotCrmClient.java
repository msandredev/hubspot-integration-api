package br.com.msandredev.hubspotintegrationapi.infra.client;

import br.com.msandredev.hubspotintegrationapi.application.dto.request.HubSpotContactRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hubspot-crm-client", url = "${hubspot.api.base-url}")
public interface HubSpotCrmClient {

    @PostMapping(
            value = "${hubspot.api.create-contact}",
            produces = "application/json",
            consumes = "application/json"
    )
    ResponseEntity<String> createContact(
            @RequestBody HubSpotContactRequest contact,
            @RequestHeader("Authorization") String accessToken
    );
}
