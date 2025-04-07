package br.com.msandredev.hubspotintegrationapi.dto.contact;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LifecycleStage {
    SUBSCRIBER("subscriber"),
    LEAD("lead"),
    MARKETING_QUALIFIED_LEAD("marketingqualifiedlead"),
    SALES_QUALIFIED_LEAD("salesqualifiedlead"),
    OPPORTUNITY("opportunity"),
    CUSTOMER("customer");

    private final String hubspotValue;

    LifecycleStage(String hubspotValue) {
        this.hubspotValue = hubspotValue;
    }

    @JsonValue
    public String getHubspotValue() {
        return hubspotValue;
    }

    @JsonCreator
    public static LifecycleStage fromString(String value) {
        for (LifecycleStage stage : values()) {
            if (stage.hubspotValue.equalsIgnoreCase(value)) {
                return stage;
            }
        }
        throw new IllegalArgumentException("Valor inválido para LifecycleStage: " + value);
    }
}
