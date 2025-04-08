package br.com.msandredev.hubspotintegrationapi.application.controller;

import br.com.msandredev.hubspotintegrationapi.application.service.HubSpotWebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hubspot")
@Tag(name = "HubSpot Webhook Controller", description = "Controller responsável por gerenciar webhooks do HubSpot.")
public class HubSpotWebhookController {

    @Autowired
    private HubSpotWebhookService service;

    @Operation(
            summary = "Processar webhook do HubSpot",
            description = "Valida e processa os eventos recebidos via webhook do HubSpot.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload do webhook",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de criação de contato",
                                            value = """
                    {
                      "objectId": 12345,
                      "propertyName": "email",
                      "propertyValue": "novo@exemplo.com",
                      "eventType": "contact.creation",
                      "subscriptionType": "contact.propertyChange",
                      "subscriptionId": 123,
                      "portalId": 456,
                      "occurredAt": 1657825200000,
                      "attemptNumber": 1
                    }
                    """
                                    ),
                                    @ExampleObject(
                                            name = "Exemplo de atualização de contato",
                                            value = """
                    {
                      "objectId": 67890,
                      "propertyName": "phone",
                      "propertyValue": "+5511999999999",
                      "eventType": "contact.propertyChange",
                      "subscriptionType": "contact.propertyChange",
                      "subscriptionId": 124,
                      "portalId": 456,
                      "occurredAt": 1657825300000,
                      "attemptNumber": 1,
                      "changeSource": "API",
                      "oldValue": "+5511888888888"
                    }
                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Webhook processado com sucesso",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = "\"Webhook recebido e processado com sucesso\""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Assinatura inválida ou requisição não autenticada",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = "\"Assinatura do webhook inválida\""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição malformada",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = "\"Timestamp inválido ou payload ausente\""
                                    )
                            )
                    )
            }
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
