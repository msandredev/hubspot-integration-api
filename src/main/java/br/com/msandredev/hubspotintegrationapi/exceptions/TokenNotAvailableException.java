package br.com.msandredev.hubspotintegrationapi.exceptions;

public class TokenNotAvailableException extends RuntimeException {
    public TokenNotAvailableException(String message) {
        super(message);
    }
}
