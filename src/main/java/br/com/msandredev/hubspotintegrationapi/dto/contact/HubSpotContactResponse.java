package br.com.msandredev.hubspotintegrationapi.dto.contact;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HubSpotContactResponse(
        @JsonProperty("id") String id,
        @JsonProperty("properties") HubSpotContactPropertiesResponse properties
) {
}
