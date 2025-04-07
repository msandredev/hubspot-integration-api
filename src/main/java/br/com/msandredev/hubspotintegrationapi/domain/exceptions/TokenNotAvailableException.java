package br.com.msandredev.hubspotintegrationapi.domain.exceptions;

public class TokenNotAvailableException extends RuntimeException {
    public TokenNotAvailableException(String message) {
        super(message);
    }
}
