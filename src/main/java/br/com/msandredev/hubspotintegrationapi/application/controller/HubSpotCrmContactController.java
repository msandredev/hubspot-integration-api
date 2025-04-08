package br.com.msandredev.hubspotintegrationapi.application.controller;

import br.com.msandredev.hubspotintegrationapi.application.dto.exceptions.ErrorResponse;
import br.com.msandredev.hubspotintegrationapi.application.dto.request.HubSpotContactRequest;
import br.com.msandredev.hubspotintegrationapi.application.service.HubSpotCrmContactService;
import br.com.msandredev.hubspotintegrationapi.domain.exceptions.TokenNotAvailableException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/hubspot/contacts")
@Tag(name = "HubSpot CRM Contact Controller", description = "Controller responsável por gerenciar contatos no CRM do HubSpot.")
public class HubSpotCrmContactController {

    @Autowired
    private HubSpotCrmContactService crmService;

    @Operation(
        summary = "Criar contato no HubSpot",
        description = "Cria um novo contato no CRM do HubSpot com base nos dados fornecidos."
    )
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
