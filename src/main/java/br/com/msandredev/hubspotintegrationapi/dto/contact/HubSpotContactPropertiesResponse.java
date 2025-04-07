package br.com.msandredev.hubspotintegrationapi.dto.contact;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HubSpotContactPropertiesResponse(
        @JsonProperty("email") String email,
        @JsonProperty("firstname") String firstName,
        @JsonProperty("lastname") String lastName,
        @JsonProperty("phone") String phone,
        @JsonProperty("company") String company,
        @JsonProperty("website") String website,
        @JsonProperty("lifecyclestage") String lifecycleStage,
        @JsonProperty("createdate") String createDate,
        @JsonProperty("lastmodifieddate") String lastModifiedDate
) {
}
