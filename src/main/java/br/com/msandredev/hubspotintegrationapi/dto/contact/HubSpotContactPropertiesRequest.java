package br.com.msandredev.hubspotintegrationapi.dto.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

public record HubSpotContactPropertiesRequest(
        @Email(message = "Email deve ser válido")
        @Max(value = 255, message = "Email não pode exceder 255 caracteres")
        @JsonProperty("email")
        String email,

        @Max(value = 100, message = "Firstname não pode exceder 100 caracteres")
        @JsonProperty("firstname")
        String firstName,

        @Max(value = 100, message = "Lastname não pode exceder 100 caracteres")
        @JsonProperty("lastname")
        String lastName,

        @Pattern(regexp = "^\\+?[0-9\\s()-]*$", message = "Telefone deve ser válido")
        @Max(value = 30, message = "Phone não pode exceder 30 caracteres")
        @JsonProperty("phone")
        String phone,

        @Max(value = 100, message = "Company não pode exceder 100 caracteres")
        @JsonProperty("company")
        String company,

        @Max(value = 255, message = "Website não pode exceder 255 caracteres")
        @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$", message = "Website deve ser uma URL válida")
        @JsonProperty("website")
        String website,

        @JsonProperty("lifecyclestage")
        LifecycleStage lifecycleStage
) {
    public HubSpotContactPropertiesRequest {
        if (email == null && firstName == null && lastName == null) {
            throw new IllegalArgumentException("Pelo menos um destes campos deve ser informado: email, firstname ou lastname");
        }
    }
}

