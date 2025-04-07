package br.com.msandredev.hubspotintegrationapi.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HubSpotContactResponse(
        @JsonProperty("id") String id,
        @JsonProperty("properties") HubSpotContactPropertiesResponse properties
) {
}
