package br.com.msandredev.hubspotintegrationapi.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HubSpotContactLifecycleStageEnum {
    SUBSCRIBER("subscriber"),
    LEAD("lead"),
    MARKETING_QUALIFIED_LEAD("marketingqualifiedlead"),
    SALES_QUALIFIED_LEAD("salesqualifiedlead"),
    OPPORTUNITY("opportunity"),
    CUSTOMER("customer");

    private final String hubspotValue;

    HubSpotContactLifecycleStageEnum(String hubspotValue) {
        this.hubspotValue = hubspotValue;
    }

    @JsonValue
    public String getHubspotValue() {
        return hubspotValue;
    }

    @JsonCreator
    public static HubSpotContactLifecycleStageEnum fromString(String value) {
        for (HubSpotContactLifecycleStageEnum stage : values()) {
            if (stage.hubspotValue.equalsIgnoreCase(value)) {
                return stage;
            }
        }
        throw new IllegalArgumentException("Valor inválido para LifecycleStage: " + value);
    }
}
