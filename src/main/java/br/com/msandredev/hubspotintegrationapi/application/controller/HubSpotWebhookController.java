package br.com.msandredev.hubspotintegrationapi.application.controller;

import br.com.msandredev.hubspotintegrationapi.application.service.HubSpotWebhookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/hubspot")
@Tag(name = "HubSpot Webhook Controller", description = "Controller responsável por gerenciar webhooks do HubSpot.")
public class HubSpotWebhookController {

    @Autowired
    private HubSpotWebhookService service;

    @Operation(
        summary = "Processar webhook do HubSpot",
        description = "Valida e processa os eventos recebidos via webhook do HubSpot."
    )
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("X-HubSpot-Signature-v3") String signature,
            @RequestHeader("X-HubSpot-Request-Timestamp") String timestamp,
            @RequestBody String rawBody,
            HttpServletRequest request) {

        return service.validateSignature(signature, timestamp, rawBody, request);
    }
}
