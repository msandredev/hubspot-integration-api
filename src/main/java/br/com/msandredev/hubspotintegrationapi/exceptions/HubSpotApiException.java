package br.com.msandredev.hubspotintegrationapi.exceptions;

public class HubSpotApiException extends RuntimeException {
    public HubSpotApiException(String message) {
        super(message);
    }
}
