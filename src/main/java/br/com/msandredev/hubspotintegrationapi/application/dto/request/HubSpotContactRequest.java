package br.com.msandredev.hubspotintegrationapi.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HubSpotContactRequest(
        @JsonProperty("properties") HubSpotContactPropertiesRequest properties,
        @JsonProperty("lifecyclestage") HubSpotContactLifecycleStageEnum hubSpotContactLifecycleStageEnum
        ) {}