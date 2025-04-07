package br.com.msandredev.hubspotintegrationapi.application.dto.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;

import java.util.Map;
import java.util.UUID;

public record HubSpotErrorResponse(
        String status,
        String message,
        String correlationId,
        String category,
        Map<String, Object> context
) {
    public static HubSpotErrorResponse fromFeignException(FeignException e) {
        try {
            return new ObjectMapper().readValue(e.contentUTF8(), HubSpotErrorResponse.class);
        } catch (Exception ex) {
            return new HubSpotErrorResponse(
                    "error",
                    "Failed to parse error response: " + e.contentUTF8(),
                    UUID.randomUUID().toString(),
                    "PARSE_ERROR",
                    null
            );
        }
    }
}