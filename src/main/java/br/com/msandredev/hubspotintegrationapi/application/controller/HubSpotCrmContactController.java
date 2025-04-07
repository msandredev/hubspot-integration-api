package br.com.msandredev.hubspotintegrationapi.application.controller;

import br.com.msandredev.hubspotintegrationapi.application.dto.request.HubSpotContactRequest;
import br.com.msandredev.hubspotintegrationapi.application.dto.exceptions.ErrorResponse;
import br.com.msandredev.hubspotintegrationapi.domain.exceptions.TokenNotAvailableException;
import br.com.msandredev.hubspotintegrationapi.application.service.HubSpotCrmContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hubspot/contacts")
@RequiredArgsConstructor
public class HubSpotCrmContactController {

    private final HubSpotCrmContactService crmService;

    @PostMapping
    public ResponseEntity<?> createContact(@Valid @RequestBody HubSpotContactRequest request) {
        try {
            return ResponseEntity.ok(crmService.createContact(request));
        } catch (TokenNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "AUTH_REQUIRED",
                            "Autenticação com HubSpot necessária. Acesse /api/hubspot/auth-url"
                    ));
        }
    }
}