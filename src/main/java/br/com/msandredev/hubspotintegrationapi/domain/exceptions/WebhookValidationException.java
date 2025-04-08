package br.com.msandredev.hubspotintegrationapi.domain.exceptions;

public class WebhookValidationException extends RuntimeException {
    public WebhookValidationException(String message) {
        super(message);
    }
}
