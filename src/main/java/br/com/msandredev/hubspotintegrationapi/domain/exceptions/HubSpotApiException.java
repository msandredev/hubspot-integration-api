package br.com.msandredev.hubspotintegrationapi.domain.exceptions;

public class HubSpotApiException extends RuntimeException {
    public HubSpotApiException(String message) {
        super(message);
    }
}
