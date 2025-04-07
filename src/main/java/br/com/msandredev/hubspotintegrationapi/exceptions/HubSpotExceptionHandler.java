package br.com.msandredev.hubspotintegrationapi.exceptions;

import br.com.msandredev.hubspotintegrationapi.dto.exceptions.ErrorResponse;
import br.com.msandredev.hubspotintegrationapi.util.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class HubSpotExceptionHandler {

    public static ResponseEntity<ErrorResponse> handleHubSpotApiException(HubSpotApiException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse(ErrorMessages.HUBSPOT_ERROR, e.getMessage()));
    }

    public static ResponseEntity<ErrorResponse> handleInternalException(Exception e) {
        log.error("Internal error: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(ErrorMessages.INTERNAL_ERROR, ErrorMessages.INTERNAL_PROCESSING_ERROR));
    }
}
