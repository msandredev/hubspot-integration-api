package br.com.msandredev.hubspotintegrationapi.dto.contact;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HubSpotContactRequest(
        @JsonProperty("properties") HubSpotContactPropertiesRequest properties
) {}